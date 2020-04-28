# karpovvly.github.io


## Service words
**var** : Объявление переменной

**if** : Условный оператор

**while** : Цикл без счетчика

**for** : Цикл с счетчиком

**Array** : Оъект Массив

**List** : Оъект Двусвязный список

**Hash** : Оъект Хеш-таблица

**print(oject)** : Вывод значения

**print()** : Вывод символа пробел

**println(object)** : Вывод значения с переносом строки

**println()** : Вывод символа переноса строки

**true** : Логическая истина

**false** : Логическая ложь
## Calculations

```js
var result
var a = (5-9) * -7

var b
b = (4 + 6)/(2-4.5)

result = (-a-b) * (a + b * b)

println(result)
```

```
-1056
```


## Array

### Number Array

```js
var array ~ Array

for(var i = 0 , i < 5 , i = i + 1)
{
  array:add(i*i)
}

for(var i = 0 , i < array:getSize() , i = i + 1)
{
  println(array:get(i))
}
```
```
0
1
4
9
16
```

### Boolean Array

```js
var array ~ Array

for(var i = 0 , i < 5 , i = i +1)
{
  if(i % 2 == 0)
  {
    array:add(true)
  }
  else
  {
    array:add(false)
  }
}

for(var i = 0 , i < array:getSize() , i = i + 1)
{
  println(array:get(i))
}
```
```
true
false
true
false
true
```

### Custom Array



```js
var array ~ Array

for(var i = 0 , i < 5 , i = i +1)
{
  if(i % 2 == 0)
  {
    array:add(true)
  }
  else
  {
    array:add((-25 - i) * i)
  }
}

for(var i = 0 , i < array:getSize() , i = i + 1)
{
  println(array:get(i))
}

```
```
true
-26
true
-84
true
```

### Multidimensional Array

```js
var array ~ Array

for(var i = 0 , i < 6 , i = i + 1)
{
  var bufferArray ~ Array
  var it
  
  for(it = 0,it < i + 2, it = it + 1)
  {
    bufferArray:add(it + (8-i)*7)
  }
  
  array:add(bufferArray)

}

for(var i = 0 , i < 6 , i = i + 1)
{
  var bufferArray = array:get(i)
  var j = 0
  
  while(j < bufferArray:getSize())
  {
    print(bufferArray:get(j))
    print()
    j = j + 1
  }
  println()
}

```
```
56 57
49 50 51
42 43 44 45
35 36 37 38 39
28 29 30 31 32 33
21 22 23 24 25 26 27
```


### Functions

**add(object)**         : Добавление элемента

**set(object, number)** : Изменение элемента на заданной позиции
  
**get(number)**         : Возвращение элемента на позиции

**remove(number)**      : Удаление элемента на заданной позиции

**setSize(number)**     : Установка Размера массива, элементы заполняются 0

**getSize()**           : Возвращение размера массива

**isEmpty()**           : Возвращение true, если массив пуст, false в обратном случае

**clear()**             : Очищение массива


## List

```js
var linkedList ~ List

linkedList:addBackward(3)
linkedList:addBackward(4)
linkedList:addBackward(5)
linkedList:addForward(2)
linkedList:addForward(1)

for(var it = 0 , it < linkedList:getSize() , it = it + 1)
{
  println(linkedList:get(it))
}
```
```
1
2
3
4
5
```

### Functions

**addForward(object)**         : Добавление элемента в начало списка

**addBackward(object)**         : Добавление элемента в конец списка

**add(object, number)**         : Добавление элемента на заданную позицию

**getFirst()**         : Возвращение первого элемента списка

**getLast()**         : Возвращение последнего элемента списка
  
**get(number)**         : Возвращение элемента на заданной позиции

**set(object, number)** : Изменение элемента на заданной позиции

**removeFirst()**      : Удаление первого элемента списка

**removeLast()**      : Удаление последнего элемента списка

**remove(number)**      : Удаление элемента на заданной позиции

**peek()**         : Возвращение последнего элемента списка

**pop()**         : Возвращение и удаление последнего элемента списка

**getSize()**           : Возвращение размера списка

**isEmpty()**           : Возвращение true, если список пуст, false в обратном случае

**clear()**             : Очищение списка


## Hash

```js
var hashMap ~ Hash

hashMap:put(34, 26-89)
hashMap:put(0, (5 + 7))
hashMap:put(1, (5 + 7) * 9)

println(hashMap:get(34))
println(hashMap:get(5))
println(hashMap:get(1))

print(hashMap:containsKey(4))
print()
print(hashMap:containsValue((5 + 7) * 9))
```
```
-63

108
false true
```

### Functions

**put(key, value)**         : Добавление элемента по заданному ключу

**replace(key, value)**     : Изменение элемента по заданному ключу
  
**get(key)**                : Возвращение элемента по заданному ключу 

**remove(key)**             : Удаление элемента по заданному ключу

**containsKey(key)**        : Возвращение true, если key существует, false в обратном случае

**ContainsValue(value)**    : Возвращение true, если value существует, false в обратном случае

**getSize()**               : Возвращение размера хеш-таблицы

**isEmpty()**               : Возвращение true, если хеш-таблица пуста, false в обратном случае

**clear()**                 : Очищение хеш-таблицы


## Example

```js
var array ~ Array

array:add(11)
array:add(9)
array:add(2)
array:add(0)
array:add(67)

var iterator = 0

while(iterator != array:getSize())
{
  println(array:get(iterator))
  iterator = iterator + 1
}

for(var i = array:getSize() - 1 , i > 0 , i = i - 1)
{
  for(var j = 0 , j < i , j = j + 1)
  {
    if(array:get(j) > array:get( j + 1))
    {
      var buffer = array:get(j)
      array:set(array:get(j+1), j)
      array:set(buffer, j+1)
    }
  }
}

iterator = 0
while(iterator != array:getSize())
{
  println(array:get(iterator))
  iterator = iterator + 1
}
```
```
Исходный массив
11
9
2
0
67
```
```
Отсортированный массив
0
2
9
11
67
```
