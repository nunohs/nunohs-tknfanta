@echo off
echo Checking Java installation...
java -version
if errorlevel 1 (
    echo Java is not installed or not in PATH
    pause
    exit /b 1
)

echo Building project...
call mvn clean compile
if errorlevel 1 (
    echo Build failed
    pause
    exit /b 1
)

echo Running game...
java -cp "target\classes;%USERPROFILE%\.m2\repository\io\github\eleanor-em\bagel\1.9.3\bagel-1.9.3.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-assimp\3.3.1\lwjgl-assimp-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-assimp\3.3.1\lwjgl-assimp-3.3.1-natives-windows.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows.jar;%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows.jar" -Djava.library.path="%USERPROFILE%\.m2\repository\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows.jar" TKNFanta
if errorlevel 1 (
    echo Game failed to start
    pause
    exit /b 1
) 