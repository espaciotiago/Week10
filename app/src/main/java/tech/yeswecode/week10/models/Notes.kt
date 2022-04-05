package tech.yeswecode.week10.models

data class Notes (
    val id: String,
    val note: String,
    val date: Long,
    val userId: String
) {
    override fun toString(): String {
        return "${userId}: ${note}\n"
    }
}