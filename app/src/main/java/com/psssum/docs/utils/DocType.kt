package com.psssum.docs.utils

enum class DocType(val type: Int) {
    TEXT(1),
    ARCHIVE(2),
    GIF(3),
    IMAGE(4),
    AUDIO(5),
    VIDEO(6),
    BOOK(7),
    UNDEFINED(8),
    NONE(0)
}

//1 — текстовые документы;
//2 — архивы;
//3 — gif;
//4 — изображения;
//5 — аудио;
//6 — видео;
//7 — электронные книги;
//8 — неизвестно.