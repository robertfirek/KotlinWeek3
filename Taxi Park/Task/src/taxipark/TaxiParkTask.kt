package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.minus(trips.map { trip -> trip.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        when {
            (minTrips > 0) -> trips.flatMap { trip -> trip.passengers }
                    .fold(mapOf<Passenger, Int>()) { numberOfTripsPerPassenger, passenger ->
                        val numberOfTrips = numberOfTripsPerPassenger.getOrDefault(passenger, 0) + 1
                        numberOfTripsPerPassenger + Pair(passenger, numberOfTrips)
                    }
                    .filterValues { numberOfTrips -> numberOfTrips >= minTrips }
                    .keys.toSet()
            else -> allPassengers
        }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips.filter { trip -> trip.driver == driver }
                .flatMap { trip -> trip.passengers }
                .fold(mapOf<Passenger, Int>()) { numberOfTripsPerPassenger, passenger ->
                    val numberOfTrips = numberOfTripsPerPassenger.getOrDefault(passenger, 0) + 1
                    numberOfTripsPerPassenger + Pair(passenger, numberOfTrips)
                }
                .filterValues { numberOfTrips -> numberOfTrips > 1 }
                .keys.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        trips.fold(mapOf<Passenger, List<Trip>>()) { tripsPerPassenger, trip ->
            tripsPerPassenger + trip.passengers.map { passenger ->
                passenger to listOf(trip) + tripsPerPassenger.getOrDefault(passenger, listOf())
            }
        }.mapValues { (_, trips) ->
            trips.count { trip -> (trip.discount ?: 0.0) > 0.0 } / trips.size.toDouble()
        }.filterValues { percentageOfTripsWithDiscount ->
            percentageOfTripsWithDiscount > 0.5
        }.keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? =
        trips.map { trip ->
            (trip.duration / 10 * 10)..trip.duration / 10 * 10 + 9 to trip
        }.groupBy { it.first }.maxBy { it.value.size }?.key

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {

    val numberOfTopTwentyPercentDrivers = (allDrivers.size * 0.2).toInt()
    if (numberOfTopTwentyPercentDrivers > 0 && trips.isNotEmpty()) {
        val incomePerDriver = trips
                .fold(mapOf<Driver, Double>()) { incomePerDriver, trip ->
                    incomePerDriver + Pair(trip.driver, incomePerDriver.getOrDefault(trip.driver, 0.0) + trip.cost)
                }
                .toList()
                .sortedWith(compareByDescending { it.second })
        val incomePerDriverParetoSplit = incomePerDriver.withIndex().partition { it.index < numberOfTopTwentyPercentDrivers }

        val incomeForTopTwentyPercentDrivers = incomePerDriverParetoSplit.first.sumByDouble { it.value.second }
        val incomeForBottomEightyPercentDrivers = incomePerDriverParetoSplit.second.sumByDouble { it.value.second }

        return incomeForTopTwentyPercentDrivers >= 4 * incomeForBottomEightyPercentDrivers
    }

    return false
}