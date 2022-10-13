package com.github.lucascalheiros.data_drive_file.utils


class DriveQueryBuilder {

    private val mClauseList = mutableListOf<Clause>()

    fun nameContains(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(NAME_PROPERTY, value, ClauseType.CONTAINS))
        return this
    }

    fun nameEquals(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(NAME_PROPERTY, value, ClauseType.EQUALS))
        return this
    }

    fun nameNotContains(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(NAME_PROPERTY, value, ClauseType.NOT_CONTAINS))
        return this
    }

    fun nameNotEquals(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(NAME_PROPERTY, value, ClauseType.NOT_EQUALS))
        return this
    }

    fun mimeTypeEquals(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(MIME_TYPE_PROPERTY, value, ClauseType.EQUALS))
        return this
    }

    fun mimeTypeNotEquals(value: String): DriveQueryBuilder {
        mClauseList.add(Clause(MIME_TYPE_PROPERTY, value, ClauseType.NOT_EQUALS))
        return this
    }

    fun build(): String {
        return mClauseList.joinToString(CLAUSE_SEPARATOR) { it.toQuery() }
    }

    data class Clause(
        val property: String,
        val value: String,
        val clauseType: ClauseType
    ) {
        fun toQuery(): String {
            return when (clauseType) {
                ClauseType.CONTAINS -> "$property contains '$value'"
                ClauseType.EQUALS -> "$property = '$value'"
                ClauseType.NOT_CONTAINS -> "not $property contains '$value'"
                ClauseType.NOT_EQUALS -> "$property != '$value'"
            }
        }
    }

    enum class ClauseType {
        CONTAINS,
        EQUALS,
        NOT_CONTAINS,
        NOT_EQUALS
    }

    companion object {
        private const val NAME_PROPERTY = "name"
        private const val MIME_TYPE_PROPERTY = "mimeType"
        private const val CLAUSE_SEPARATOR = " and "
    }

}