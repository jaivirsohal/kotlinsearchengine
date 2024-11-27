package websearch
import org.jsoup.nodes.Document

//URl class
class URL(val url: String){
  override fun toString(): String {
    return this.url
  }
}

//Webpage class
class WebPage(val content: Document) {
  fun extractWords(): List<String> {
    return this.content
      .text()
      .split("\\W+".toRegex())
      .filter { it.isNotBlank() }
      .map { it.lowercase() }
  }
}
