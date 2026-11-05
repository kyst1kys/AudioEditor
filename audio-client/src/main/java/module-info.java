module com.nievierov.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires static lombok;

    // 1. Експорт пакетів
    exports com.nievierov.client;
    exports com.nievierov.client.controller;
    exports com.nievierov.client.service;
    exports com.nievierov.client.engine;

    // 2. Відкриваємо головний пакет та контролери для JavaFX (щоб FXMLLoader завантажував FXML)
    opens com.nievierov.client to javafx.fxml;
    opens com.nievierov.client.controller to javafx.fxml, com.fasterxml.jackson.databind;

    // 3. Відкриваємо engine для Jackson (щоб ObjectMapper міг парсити AudioClip та AudioTrack)
//    opens com.nievierov.client.engine to com.fasterxml.jackson.databind;
}