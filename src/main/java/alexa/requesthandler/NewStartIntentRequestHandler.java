package alexa.requesthandler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NewStartIntentRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("NewstartIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return Optional.empty();
    }
}
