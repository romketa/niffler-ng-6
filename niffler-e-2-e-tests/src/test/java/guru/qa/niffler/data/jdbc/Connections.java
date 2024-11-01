package guru.qa.niffler.data.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;

public class Connections {
  private Connections() {
  }

  private static final Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

  @Nonnull
  public static JdbcConnectionHolder holder(String jdbcUrl) {
    return holders.computeIfAbsent(
        jdbcUrl,
        key -> new JdbcConnectionHolder(
            DataSources.dataSource(jdbcUrl)
        )
    );
  }

  @Nonnull
  public static JdbcConnectionHolders holders(String... jdbcUrl) {
    List<JdbcConnectionHolder> result = new ArrayList<>();
    for (String url : jdbcUrl) {
      result.add(holder(url));
    }
    return new JdbcConnectionHolders(result);
  }

  public static void closeAllConnections() {
    holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
  }
}
