@echo off

setlocal EnableDelayedExpansion

set SMART_OPTS=-Dfile.encoding=UTF-8
set SMART_OPTS=%SMART_OPTS% -Dsmart.home=%SMART_HOME%

set CLASS_PATH=%SMART_HOME%\config
for %%i in (%SMART_HOME%\lib\*.jar) do (
    set CLASS_PATH=!CLASS_PATH!;%%i
)

set INVOKER_CLASS=smart.generator.Invoker

if not "%1" == "" (
    goto %1
) else (
    echo ------------------------------------------------------------
    echo                        Smart Commands
    echo ------------------------------------------------------------
    echo smart create-app                       :   Create App
    echo smart create-entity ^<entity-name^>      :   Create Entity
    echo smart create-service ^<service-name^>    :   Create Service
    echo smart create-action ^<action-name^>      :   Create Action
    echo smart create-page ^<page-name^>          :   Create Page
    echo smart create-crud ^<crud-name^>          :   Create CRUD
    echo smart load-dict ^<dict-path^>            :   Load Dict
    echo smart run-test                         :   Run Test
    echo smart run-app                          :   Run App
    echo smart build-app                        :   Bulid App
    echo ------------------------------------------------------------
    goto end
)

::----------------------------------------------------------------------------------------------------
:create-app
::----------------------------------------------------------------------------------------------------
set COMMAND_CLASS=smart.generator.command.impl.CreateAppCommand

:app_name
set /p APP_NAME="1/3 - Name: "
if "%APP_NAME%"=="" (
    goto app_name;
)

:app_group
set /p APP_GROUP="2/3 - Group: "
if "%APP_GROUP%"=="" (
    goto app_group;
)

set /p APP_PACKAGE="3/3 - Package: [%APP_GROUP%.%APP_NAME%] "
if "%APP_PACKAGE%"=="" (
    set APP_PACKAGE=%APP_GROUP%.%APP_NAME%
)

set COMMAND_PARAMS=%APP_NAME% %APP_GROUP% %APP_PACKAGE%
goto java

::----------------------------------------------------------------------------------------------------
:create-entity
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.CreateEntityCommand

set ENTITY_NAME=%2

set COMMAND_PARAMS=%ENTITY_NAME%
goto java

::----------------------------------------------------------------------------------------------------
:create-service
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.CreateServiceCommand

set SERVICE_NAME=%2

set COMMAND_PARAMS=%SERVICE_NAME%
goto java

::----------------------------------------------------------------------------------------------------
:create-action
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.CreateActionCommand

set ACTION_NAME=%2

set COMMAND_PARAMS=%ACTION_NAME%
goto java

::----------------------------------------------------------------------------------------------------
:create-page
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.CreatePageCommand

set PAGE_NAME=%2

set COMMAND_PARAMS=%PAGE_NAME%
goto java

::----------------------------------------------------------------------------------------------------
:create-crud
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.CreateCRUDCommand

set CRUD_NAME=%2

set COMMAND_PARAMS=%CRUD_NAME%
goto java

::----------------------------------------------------------------------------------------------------
:load-dict
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

set COMMAND_CLASS=smart.generator.command.impl.LoadDictCommand

set DICT_PATH=%2

set COMMAND_PARAMS=%DICT_PATH%
goto java

::----------------------------------------------------------------------------------------------------
:run-test
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

call mvn test
goto end

::----------------------------------------------------------------------------------------------------
:build-app
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

call mvn package
goto end

::----------------------------------------------------------------------------------------------------
:run-app
::----------------------------------------------------------------------------------------------------
if not exist pom.xml (
    goto error
)

call mvn tomcat7:run
goto end

::----------------------------------------------------------------------------------------------------
:java
::----------------------------------------------------------------------------------------------------
java %SMART_OPTS% -cp "%CLASS_PATH%" %INVOKER_CLASS% %COMMAND_CLASS% "%CD%" %COMMAND_PARAMS%
goto end

::----------------------------------------------------------------------------------------------------
:error
::----------------------------------------------------------------------------------------------------
echo [ERROR] Please use this command on the project directory.
goto end

::----------------------------------------------------------------------------------------------------
:end
::----------------------------------------------------------------------------------------------------
endlocal