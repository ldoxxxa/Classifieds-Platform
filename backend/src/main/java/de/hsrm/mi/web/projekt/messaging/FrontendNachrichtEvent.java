package de.hsrm.mi.web.projekt.messaging;

public class FrontendNachrichtEvent {

    public enum EventTyp { ANZEIGE }
    public enum Operation { CREATE, UPDATE, DELETE }

    private EventTyp typ;
    private Long id;
    private Operation operation;

    public FrontendNachrichtEvent(EventTyp typ, Long id, Operation operation) {
        this.typ = typ;
        this.id = id;
        this.operation = operation;
    }

    public EventTyp getTyp() { return typ; }
    public Long getId() { return id; }
    public Operation getOperation() { return operation; }
}