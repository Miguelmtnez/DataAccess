CREATE OR REPLACE FUNCTION available_flights_mmr(
    origin_iata varchar,
    destination_iata varchar,
    travel_date date
)
RETURNS TABLE (
    flight_code varchar,
    airline_name varchar,
    departure time,
    arrival time,
    available_seats bigint
)
LANGUAGE sql
STABLE
AS $function$
    SELECT f.flight_code,
           al.name AS airline_name,
           f.departure,
           f.arrival,
           (p.total_seats - COUNT(t.ticket_number)
               FILTER (WHERE t.date_of_cancellation IS NULL))::bigint AS available_seats
    FROM flights f
    JOIN airports source_airport ON source_airport.code = f.source
    JOIN airports destination_airport ON destination_airport.code = f.destination
    JOIN airlines al ON al.airlineid = f.airlineid
    JOIN planes p ON p.plane_id = f.plane
    LEFT JOIN tickets t
        ON t.flight_code = f.flight_code
       AND t.date_of_travel = travel_date
    WHERE source_airport.iata = origin_iata
      AND destination_airport.iata = destination_iata
    GROUP BY f.flight_code, al.name, f.departure, f.arrival, p.total_seats
    HAVING p.total_seats - COUNT(t.ticket_number)
        FILTER (WHERE t.date_of_cancellation IS NULL) > 0
    ORDER BY f.departure, f.flight_code
$function$;
