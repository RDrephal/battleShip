package alexa.steamhandler;

import alexa.requesthandler.*;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class AlexaBattleShipStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(new ShotIntentRequestHandler(), new NewStartIntentRequestHandler(), new CancelandStopIntentHandler(),  new HelpIntentHandler(), new LaunchRequestHandler(), new SessionEndedRequestHandler())
                .build();
    }

    public AlexaBattleShipStreamHandler() {
        super(getSkill());
    }
}
