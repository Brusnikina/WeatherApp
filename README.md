# Приложение прогноза погоды
Приложение отображает текущий прогноз погоды, почасовой прогноз на ближайшие 48 часов и прогноз на неделю. Приложение предоставляет прогноз погоды для текущего местоположения (если пользователь дал соответствующее разрешение) или для выбранного пользователем города. 

В качестве API использовался сервис OpenWeatherMap, а именно [One Call API](https://openweathermap.org/api/one-call-api).

### Стек технологий 
- MVVM
- Retrofit
- Room
- RxJava

### Краткое пояснение
При запуске приложение пытается определить местоположение пользователя, чтобы предоставить точный прогноз (метод **void requestLocation()**). Полученные координаты передаются в метод **void makeCall(double lat, double lon)**. В ответ на запрос сервер возвращает JSON, который преобразуется в объект WeatherDTO. Этот объект содержит избыточные данные, которые нет необходимости хранить в БД. На основе данных из WeatherDTO формируем объекты типа CurrentForecast, HourlyForecast, DailyForecast, которые являются Entity. Очищаем БД от устаревших данных и записываем новые. Определяем название текущего города по координатам (метод **String getCityByCoordinates(double lat, double lon)**) и устанавливаем его в качестве заголовка Activity.

При нажатии на SearchView пользователь может ввести название города, для которого хочет получить прогноз погоды. Метод **void callWithCityName(String city)** отвечает за получение координат выбранного города и обращение к серверу. 

### Скриншоты
Просто несколько скриншотов приложения

![screenshot](/readme_assets/Screenshot_1.png "Запрашиваем разрешение")
![screenshot](/readme_assets/Screenshot_2.png "Погода в Москве")
![screenshot](/readme_assets/Screenshot_3.png "Погода в Ростове-На-Дону")
![screenshot](/readme_assets/Screenshot_4.png "Погода в Нью-Йорке")

