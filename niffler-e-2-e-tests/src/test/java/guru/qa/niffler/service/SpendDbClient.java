package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient implements SpendClient{

  private static final Config CFG = Config.getInstance();

  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendRepository spendRepository = new SpendRepositoryJdbc();
  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.spendJdbcUrl()
  );
  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.spendJdbcUrl()
  );

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
            spendRepository.create(SpendEntity.fromJson(spend))
        )
    );
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
            spendRepository.createCategory(CategoryEntity.fromJson(category))
        )
    );
  }

  @Override
  public void removeCategory(CategoryJson category) {
    xaTransactionTemplate.execute(() -> {
      spendRepository.removeCategory(CategoryEntity.fromJson(category));
      return null;
    });
  }

  @Override
  public CategoryJson findOrCreateCategoryByUsernameAndName(String username, String name) {
    return jdbcTxTemplate.execute(
        () -> categoryDao.findCategoryByUsernameAndCategoryName(username, name)
            .map(CategoryJson::fromEntity)
            .orElse(new CategoryJson(null, name, username, false)));
  }
}
