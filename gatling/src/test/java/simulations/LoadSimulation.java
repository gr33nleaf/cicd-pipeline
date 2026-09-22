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
                nothingFor(5),                        // Warm-up: 5s pause
                atOnceUsers(10),                      // Spike: 10 simultaneous users
                rampUsers(20).during(20),             // Ramp-up: 20 users over 20s
                constantUsersPerSec(15).during(90),   // Sustained load: 15 req/s for 90s → triggers HPA
                rampUsers(80).during(30)              // Peak: 80 users over 30s → second replica visible
            )
        )
        .protocols(httpProtocol)
        .assertions(
            // Slightly relaxed thresholds: brief latency spikes are expected
            // while HPA scales up and the second pod becomes ready
            global().responseTime().max().lt(3000),
            global().successfulRequests().percent().gt(90.0)
        );
    }
}