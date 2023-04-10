@ECHO OFF 
xcopy /s /y  ".\icons"  "..\deploy_container\icons"
xcopy /s /y  ".\resources"  "..\deploy_container\resources"
xcopy /s /y ".\target\ProductManagementSystem-App_1.0.2-jar-with-dependencies.jar"  "..\deploy_container\"
xcopy /s /y ".\exe"   "..\deploy_container\exe"
