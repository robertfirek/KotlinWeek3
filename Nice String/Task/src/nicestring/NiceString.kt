package nicestring

fun String.isNice(): Boolean {
    fun doesNotContainBaOrBuOrBe() =
            this.none {
                this.contains("ba", true) ||
                        this.contains("bu", true) ||
                        this.contains("be", true)
            }

    fun containsAtLeastThreeVowels() =
            this.count { character -> character in arrayOf('a', 'e', 'i', 'o', 'u') } >= 3

    fun containsDoubleLetter() = this.fold(Pair(0, '-')) { (numberOfDuplicates, previousCharacter), character ->
        Pair(numberOfDuplicates + (if (character == previousCharacter) 1 else 0), character)
    }.first > 0


    return arrayOf(doesNotContainBaOrBuOrBe(), containsAtLeastThreeVowels(), containsDoubleLetter()).count { it } >= 2
}