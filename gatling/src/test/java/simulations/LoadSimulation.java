package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadSimulation extends Simulation {

    private static final String BASE_URL =
        System.getProperty("baseUrl", "http://localhost:8000");

    HttpProtocolBuilder httpProtocol = http
        .baseUrl(BASE_URL)
        .acceptHeader("application/json");

    ScenarioBuilder loadScenario = scenario("Random App Load Test")
        .exec(
            http("GET /random")
                .get("/random")
                .check(status().is(200))
        );

    {
        setUp(
            loadScenario.injectOpen(
                nothingFor(5),                          // Warm-up: 5s pause
                atOnceUsers(50),                        // Spike: 50 simultaneous users
                rampUsers(50).during(20),               // Ramp-up: 50 users over 20s
                constantUsersPerSec(80).during(90),     // Sustained load: 80 req/s for 90s → HPA-Trigger
                rampUsers(600).during(60)               // Peak: 600 users over 60s → HPA skaliert
            )
        )
        .protocols(httpProtocol)
        .assertions(
            global().responseTime().max().lt(3000),
            global().successfulRequests().percent().gt(90.0)
        );
    }
}