package tech.yeswecode.week10.models

data class User (
    val id: String,
    val name: String,
    val date: Long
) {
    override fun toString(): String {
        return "${name}.\n\n"
    }
}