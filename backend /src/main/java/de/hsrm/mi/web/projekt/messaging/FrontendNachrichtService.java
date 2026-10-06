package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class FrontendNachrichtService {

    private static final Logger logger = LoggerFactory.getLogger(FrontendNachrichtService.class);
    private static final String DEST = "/topic/anzeige";

    private final SimpMessagingTemplate messagingTemplate;

    public FrontendNachrichtService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEvent(FrontendNachrichtEvent ev) {
        logger.info("Sende FrontendNachrichtEvent an {}: typ={}, id={}, operation={}",
                DEST, ev.getTyp(), ev.getId(), ev.getOperation());
        messagingTemplate.convertAndSend(DEST, ev);
    }
}