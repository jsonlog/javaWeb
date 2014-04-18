package smart.generator.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import smart.framework.OrderedRunner;
import smart.framework.annotation.Order;
import smart.generator.Invoker;
import smart.generator.command.impl.CreateActionCommand;
import smart.generator.command.impl.CreateAppCommand;
import smart.generator.command.impl.CreateCRUDCommand;
import smart.generator.command.impl.CreateEntityCommand;
import smart.generator.command.impl.CreatePageCommand;
import smart.generator.command.impl.CreateServiceCommand;
import smart.generator.command.impl.LoadDictCommand;

@RunWith(OrderedRunner.class)
public class CommandTest {

    @Test
    @Order(1)
    public void createAppCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateAppCommand()); // smart create-app

        String[] params = {
            "C:\\Smart",        // Current Path
            "demo",             // App Name
            "smart",        // App Group
            "smart.demo"    // App Package = <App Group> + <App Artifact>
        };
        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }

    @Test
    @Order(2)
    public void createEntityCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateEntityCommand()); // smart create-entity <entity-name>

        String[] entityNameArray = {"product", "product-type"};
        for (String entityName : entityNameArray) {
            String[] params = {
                "C:\\Smart\\demo",  // Current Path
                entityName,         // Entity Name
            };
            boolean result = invoker.execCommand(params);
            if (!result) {
                System.err.println("执行命令出错！");
            }
        }
    }

    @Test
    @Order(3)
    public void createServiceCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateServiceCommand()); // smart create-service <service-name>

        String[] serviceNameArray = {"product", "product-type"};
        for (String serviceName : serviceNameArray) {
            String[] params = {
                "C:\\Smart\\demo",  // Current Path
                serviceName,        // Service Name
            };
            boolean result = invoker.execCommand(params);
            if (!result) {
                System.err.println("执行命令出错！");
            }
        }
    }

    @Test
    @Order(4)
    public void createActionCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateActionCommand()); // smart create-action <action-name>

        String[] actionNameArray = {"product", "product-type"};
        for (String actionName : actionNameArray) {
            String[] params = {
                "C:\\Smart\\demo",  // Current Path
                actionName,         // Action Name
            };
            boolean result = invoker.execCommand(params);
            if (!result) {
                System.err.println("执行命令出错！");
            }
        }
    }

    @Test
    @Order(5)
    public void createPageCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreatePageCommand()); // smart create-page <page-name>

        String[] pageNameArray = {"product", "product-type"};
        for (String pageName : pageNameArray) {
            String[] params = {
                "C:\\Smart\\demo",  // Current Path
                pageName,           // Page Name
            };
            boolean result = invoker.execCommand(params);
            if (!result) {
                System.err.println("执行命令出错！");
            }
        }
    }

    @Test
    @Order(6)
    public void createCRUDCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new CreateCRUDCommand()); // smart create-crud <crud-name>

        String[] crudNameArray = {"customer", "customer-type"};
        for (String crudName : crudNameArray) {
            String[] params = {
                "C:\\Smart\\demo",  // Current Path
                crudName,           // CRUD Name
            };
            boolean result = invoker.execCommand(params);
            if (!result) {
                System.err.println("执行命令出错！");
            }
        }
    }

    @Test
    @Order(7)
    public void loadDictCommandTest() {
        Invoker invoker = new Invoker();
        invoker.setCommand(new LoadDictCommand()); // smart load-dict <dict-path>

        String[] params = {
            "C:\\Smart\\demo",          // Current Path
            "C:\\Smart\\demo\\db.xls",  // Dict Path
        };
        boolean result = invoker.execCommand(params);
        if (!result) {
            System.err.println("执行命令出错！");
        }
    }
}
