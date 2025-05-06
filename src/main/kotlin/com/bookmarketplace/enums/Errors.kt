package com.bookmarketplace.enums

enum class Errors(val code: String, val message: String) {
    BM00001("BM-00001","Invalid Request"),
    BM10000("BM-10000","Customer [%s] does not exist"),
    BM10001("BM-10001","Can not update book with status [%s]"),
    BM20000("BM-20000", "Book [%s] does not exist");
}

