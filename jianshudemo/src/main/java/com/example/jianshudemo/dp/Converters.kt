package com.example.jianshudemo.dp

import androidx.room.TypeConverter
import java.util.*

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 *
 * Converters
 * @author liujianming
 * @date 2023-02-09
 */
class Converters {
    @TypeConverter fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis
    @TypeConverter fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }
}