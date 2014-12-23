Задание по программированию, третий семестр
================

Интерфейсы, описанные в пункте 2 задания лежат в пакете ```ru.mipt.hometask.strings.interfaces```



Задание 3.1 - ```ru.mipt.hometask.strings.NaiveTemplateMatcher```

Задания 3.2, 4.1, 4.2 - ```ru.mipt.hometask.strings.SingleTemplateMatcher```

Задание 3.3 - ```ru.mipt.hometask.strings.StaticTemplateMatcher```

Задание 4.4 - ```ru.mipt.hometask.strings.DynamicTemplateMatcher```



Тесты:

Все тесты лежат в пакeте ```ru.mipt.hometask.strings.tests```.
Тесты на корректность являются unit-тестами, написанными с помощью фреймврка JUnit (запускать через JUnit).
Тесты на производительность запускаются из класса ```ru.mipt.hometask.strings.Main```. Сам код лежит в классе ```ru.mipt.hometask.strings.tests.PerfomanceTests```, он используют юнит тесты, и для малых величин тоже проверяет корректность.