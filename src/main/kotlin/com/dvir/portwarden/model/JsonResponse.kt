enum class Types {

    SUCCESS,
    ERROR,
    PROMPT,
    INFO

}

data class JsonResponse (

    val message: String,
    val data: Any,
    val type: Types

)