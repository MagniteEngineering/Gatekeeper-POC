package org.gatekeeper.endpoint.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.sql.Timestamp;
import java.util.Calendar;
import org.gatekeeper.endpoint.context.DataContext;
import org.gatekeeper.endpoint.model.Session;
import org.gatekeeper.endpoint.service.SessionService;
import org.gatekeeper.endpoint.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatekeeperSyncHandler implements Handler<RoutingContext> {
    private static final Logger logger = LoggerFactory.getLogger(GatekeeperSyncHandler.class);

    @Autowired
    SessionService sessionService;

    @Autowired
    public GatekeeperSyncHandler() {
    }

    @Override
    public void handle(RoutingContext routingContext) {
        DataContext dataContext = DataContext.from(routingContext);
        String sessionId = dataContext.getSessionId();
        String domain = dataContext.getDomain();
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setDomain(domain);
        session.setCreatedOn(currentTimestamp);
        sessionService.save(session);

        HttpServerResponse response = routingContext.response();
        response.setStatusCode(200);

        logger.debug("Responding with {} ", response.getStatusCode());

        response.end();
    }

}
