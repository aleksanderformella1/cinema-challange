# How to use

1. Please just run the service by `./gradlew bootRun`
2. You can test it by following commands:
```bash

curl 'localhost:8080/room' #get existing rooms to get the room ID
curl 'localhost:8080/movie' #get existing movies to get the movie ID

curl --header "Content-Type: application/json" \
     --request POST \
     --data '{
                    "roomId": "44a577de-9277-45c2-9719-17b42c149767",
                    "movieId": "24f92daa-ef57-49a0-8355-0309dcbd2475",
                    "showType": "REGULAR",
                    "threeDimGlassesRequired": false,
                    "start": "2023-06-22T16:00",
                    "duration": "PT2H"
     }' \
'localhost:8080/event/show' #add new show

curl --header "Content-Type: application/json" \
     --request POST \
     --data '{
                 "roomId": "fa006779-47fd-4c56-ac95-8a96eeb517c3",
                 "from": "2023-06-22T09:00",
                 "to": "2023-06-22T09:30"
             }' \
'localhost:8080/event/room/unavailability' #add new room unavailability event

curl 'localhost:8080/event/show?from=2023-06-22T18:00&to=2023-07-22T18:00&roomId=44a577de-9277-45c2-9719-17b42c149767' #find shows

curl 'localhost:8080/event/room?from=2023-06-22T18:02&to=2023-07-22T18:00&roomId=44a577de-9277-45c2-9719-17b42c149767' #find room events


```
