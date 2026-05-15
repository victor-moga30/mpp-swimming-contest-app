package ro.mpp2026.restclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

public class StartJavaRestClient {
    private static final String BASE_URL = "http://localhost:8080/api/events";

    public static void main(String[] args) {
        RestClient client = RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    System.out.println();
                    System.out.println("JAVA CLIENT REQUEST");
                    System.out.println(request.getMethod() + " " + request.getURI());

                    if (body != null && body.length > 0) {
                        System.out.println("Body bytes: " + body.length);
                    }

                    var response = execution.execute(request, body);

                    System.out.println("JAVA CLIENT RESPONSE");
                    System.out.println("Status: " + response.getStatusCode());

                    return response;
                })
                .build();

        try {
            System.out.println("1. Afisare toate probele");
            List<EventDTO> allEvents = client.get()
                    .uri(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EventDTO>>() {
                    });

            printEvents(allEvents);

            System.out.println();
            System.out.println("2. Creare proba noua");
            EventCreateRequest createRequest = new EventCreateRequest(
                    "Test REST 300m",
                    300,
                    9,
                    11
            );

            Long createdId = client.post()
                    .uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(createRequest)
                    .retrieve()
                    .body(Long.class);

            System.out.println("Id creat: " + createdId);

            System.out.println();
            System.out.println("3. Cautare dupa id");
            EventDTO createdEvent = client.get()
                    .uri(BASE_URL + "/{id}", createdId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(EventDTO.class);

            System.out.println(createdEvent);

            System.out.println();
            System.out.println("4. Modificare proba");
            EventUpdateRequest updateRequest = new EventUpdateRequest(
                    "Test REST 300m modificat",
                    300,
                    10,
                    12
            );

            EventDTO updatedEvent = client.put()
                    .uri(BASE_URL + "/{id}", createdId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(updateRequest)
                    .retrieve()
                    .body(EventDTO.class);

            System.out.println(updatedEvent);

            System.out.println();
            System.out.println("5. Filtrare dupa distanta");
            List<EventDTO> filteredEvents = client.get()
                    .uri(BASE_URL + "?distance={distance}", 300)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EventDTO>>() {
                    });

            printEvents(filteredEvents);

            System.out.println();
            System.out.println("6. Stergere proba creata");
            HttpStatusCode deleteStatus = client.delete()
                    .uri(BASE_URL + "/{id}", createdId)
                    .exchange((request, response) -> response.getStatusCode());

            System.out.println("Delete status: " + deleteStatus);

            System.out.println();
            System.out.println("7. Stergere din nou aceeasi proba");
            HttpStatusCode secondDeleteStatus = client.delete()
                    .uri(BASE_URL + "/{id}", createdId)
                    .exchange((request, response) -> response.getStatusCode());

            System.out.println("Second delete status: " + secondDeleteStatus);

            System.out.println();
            System.out.println("Test Java REST terminat cu succes.");
        } catch (Exception exception) {
            System.out.println("Eroare in clientul Java REST:");
            System.out.println(exception.getMessage());
        }
    }

    private static void printEvents(List<EventDTO> events) {
        if (events == null || events.isEmpty()) {
            System.out.println("Nu exista probe.");
            return;
        }

        for (EventDTO event : events) {
            System.out.println(event);
        }
    }
}