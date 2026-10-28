module com.nievierov.audio_client.audioclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    opens com.nievierov.audio_client.audioclient to javafx.fxml;
    exports com.nievierov.audio_client.audioclient;
}