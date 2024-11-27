package websearch

class SearchEngine(
  val corpus: Map<URL, WebPage>,
  var index: Map<String, List<SearchResult>> = emptyMap()
) {
  fun compileIndex() {
    // map each word to the url
    val wordPairs: List<Pair<String, URL>> = corpus
      .flatMap { (url, webPage) ->
        webPage.extractWords().map { it to url }
      }

    // group by word
    val groupedPairs: Map<String, List<URL>> = wordPairs
      .groupBy({ it.first }, { it.second })

    // did not decide to use a separate rank() function
    // sort by occurrences
    val sorted: Map<String, List<SearchResult>> = groupedPairs
      .map { (word, urls) ->
        word to urls
          .groupingBy { it }  // Group by URL
          .eachCount()  // Count occurrences of each URL
          .entries
          .sortedByDescending { it.value }  // Sort URLs by frequency
          .map { SearchResult(it.key, it.value) }
      }
      .toMap()

    this.index = sorted
  }

  fun searchFor(query: String): SearchResultsSummary {
    val results:  List<SearchResult> = index[query]?.toList() ?: emptyList()
    return SearchResultsSummary(query, results)
  }
}

class SearchResult(val url: URL, val numRefs: Int)

class SearchResultsSummary(
  val query: String,
  val results: List<SearchResult>
) {
  override fun toString(): String {
    val resultString = "Results for \"$query\":\n"
    results.forEach { resultString + ("${it.url} - ${it.numRefs} $query\\n") }
    return resultString
  }
}
