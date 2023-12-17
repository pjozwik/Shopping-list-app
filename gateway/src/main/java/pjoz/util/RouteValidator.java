package pjoz.util;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> apiOpenEndPoints = List.of(
            "/api/authenticate",
            "/api/users/register",
            "/api/adverts/all"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> apiOpenEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
