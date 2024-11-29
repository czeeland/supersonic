package com.tencent.supersonic.headless.core.translator.optimizer;

import com.tencent.supersonic.headless.api.pojo.response.DatabaseResp;
import com.tencent.supersonic.headless.api.pojo.response.SemanticSchemaResp;
import com.tencent.supersonic.headless.core.adaptor.db.DbAdaptor;
import com.tencent.supersonic.headless.core.adaptor.db.DbAdaptorFactory;
import com.tencent.supersonic.headless.core.pojo.QueryStatement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component("DbDialectOptimizer")
public class DbDialectOptimizer implements QueryOptimizer {

    @Override
    public void rewrite(QueryStatement queryStatement) {
        SemanticSchemaResp semanticSchemaResp = queryStatement.getSemanticSchemaResp();
        DatabaseResp database = semanticSchemaResp.getDatabaseResp();
        String sql = queryStatement.getSql();
        if (Objects.isNull(database) || Objects.isNull(database.getType())) {
            return;
        }
        String type = database.getType();
        DbAdaptor engineAdaptor = DbAdaptorFactory.getEngineAdaptor(type.toLowerCase());
        if (Objects.nonNull(engineAdaptor)) {
            String adaptedSql = engineAdaptor.rewriteSql(sql);
            queryStatement.setSql(adaptedSql);
        }
    }
}
