package guru.qa.niffler.data;

import static guru.qa.niffler.data.Databases.TransactionIsolation.TRANSACTION_READ_COMMITTED;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
  private Databases() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
  private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

  public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {
  }

  public record XaConsumer(Consumer<Connection> consumer, String jdbcUrl) {
  }

  public enum TransactionIsolation {
    TRANSACTION_NONE(0),
    TRANSACTION_READ_UNCOMMITTED(1),
    TRANSACTION_READ_COMMITTED(2),
    TRANSACTION_REPEATABLE_READ(4),
    TRANSACTION_SERIALIZABLE(8);

    @Getter
    final int levelIsolation;

    TransactionIsolation(int levelIsolation) {
      this.levelIsolation = levelIsolation;
    }
  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl, TransactionIsolation transactionIsolation) {
    return applyTransaction(function, jdbcUrl, transactionIsolation);
  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
    return applyTransaction(function, jdbcUrl, TRANSACTION_READ_COMMITTED);
  }

  private static <T> T applyTransaction(Function<Connection, T> function, String jdbcUrl, TransactionIsolation transactionIsolation) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setTransactionIsolation(transactionIsolation.getLevelIsolation());
      connection.setAutoCommit(false);
      T result = function.apply(connection);
      connection.commit();
      connection.setAutoCommit(true);
      return result;
    } catch (Exception e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      throw new RuntimeException(e);
    }
  }

  public static <T> T xaTransaction(XaFunction<T>... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      T result = null;
      for (XaFunction<T> action : actions) {
        result = action.function.apply(connection(action.jdbcUrl));
      }
      ut.commit();
      return result;
    } catch (Exception e) {
      try {
        ut.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }


  public static void transaction(Consumer<Connection> consumer, String jdbcUrl, TransactionIsolation transactionIsolation) {
    acceptTransaction(consumer, jdbcUrl, transactionIsolation);
  }

  public static void transaction(Consumer<Connection> consumer, String jdbcUrl) {
    acceptTransaction(consumer, jdbcUrl, TRANSACTION_READ_COMMITTED);
  }

  private static void acceptTransaction(Consumer<Connection> consumer, String jdbcUrl,
      TransactionIsolation transactionIsolation) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setTransactionIsolation(transactionIsolation.getLevelIsolation());
      connection.setAutoCommit(false);
      consumer.accept(connection);
      connection.commit();
      connection.setAutoCommit(true);
    } catch (Exception e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      throw new RuntimeException(e);
    }
  }

  public static void xaTransaction(XaConsumer... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      for (XaConsumer action : actions) {
        action.consumer.accept(connection(action.jdbcUrl));
      }
      ut.commit();
    } catch (Exception e) {
      try {
        ut.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  private static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          final String uniqId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "secret");
          dsBean.setXaProperties(props);
          dsBean.setMaxPoolSize(10);
          return dsBean;
        }
    );
  }

  private static Connection connection(String jdbcUrl) throws SQLException {
    return threadConnections.computeIfAbsent(
        Thread.currentThread().threadId(),
        key -> {
          try {
            return new HashMap<>(Map.of(
                jdbcUrl,
                dataSource(jdbcUrl).getConnection()
            ));
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    ).computeIfAbsent(
        jdbcUrl,
        key -> {
          try {
            return dataSource(jdbcUrl).getConnection();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    );
  }

  public static void closeAllConnections() {
    for (Map<String, Connection> connectionMap : threadConnections.values()) {
      for (Connection connection : connectionMap.values()) {
        try {
          if (connection != null && !connection.isClosed()) {
            connection.close();
          }
        } catch (SQLException e) {
          // NOP
        }
      }
    }
  }
}
