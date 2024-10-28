module com.example.testaktualizacji {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.testaktualizacji to javafx.fxml;
    exports com.example.testaktualizacji;
}