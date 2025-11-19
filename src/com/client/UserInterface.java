package com.client;

import com.management.*;
import com.service.*;
import com.model.*;
import com.exception.SupplyChainException;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static SupplierService supplierService = new SupplierService();
    private static ProductService productService = new ProductService();
    private static InventoryService inventoryService = new InventoryService();
    private static OrderService orderService = new OrderService();
    private static TransportationService transService = new TransportationService();
    private static WarehouseService warehouseService = new WarehouseService();
    static String loggedInUser = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserManagement userMgmt = new UserManagement();

        boolean loggedIn = false;
        boolean isAdmin = false;
        String role = null;

        System.out.println("========= Welcome to TechNex Supply Chain System =========");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");

        int first = sc.nextInt();
        sc.nextLine();
        if(first!=1 && first!=2 && first!=3) {
        	System.out.println("Invalid input, exiting.");
        	return;
        }
        if(first==3) {
        	System.out.println("Exiting...");
        	return;
        }

        try {
            if (first == 1) {
                System.out.print("Choose role (ADMIN/CUSTOMER): ");
                String newRole = sc.nextLine().trim().toUpperCase();

                if (!newRole.equals("ADMIN") && !newRole.equals("CUSTOMER")) {
                    System.out.println("Invalid role. Choose ADMIN or CUSTOMER.");
                    return;
                }

                if (newRole.equals("ADMIN")) {
                    System.out.print("Enter Company's special code: ");
                    String companyCode = sc.nextLine().trim();
                    if (!companyCode.equals("TechNex@123")) {
                        System.out.println("Invalid company code. Registration failed.");
                        return;
                    }
                }

                System.out.print("Username: ");
                String user = sc.nextLine().trim();
                System.out.print("Password: ");
                String pass = sc.nextLine().trim();

                boolean check = userMgmt.register(user, pass, newRole);
                if(check) {
                	 System.out.println("Registered successfully! Please login now.");
                }
            }

            if (first == 1 || first == 2) {
                System.out.print("Username: ");
                String user = sc.nextLine().trim();
                System.out.print("Password: ");
                String pass = sc.nextLine().trim();

                role = userMgmt.login(user, pass);
                if (role != null) {
                    loggedIn = true;
                    isAdmin = role.equalsIgnoreCase("ADMIN");
                    loggedInUser = user;
                    System.out.println("Login successful! Logged in as: " + role);
                } else {
                    System.out.println("Invalid credentials. Access denied.");
                    return;
                }
            } else {
                System.out.println("Invalid choice.");
                return;
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        

        if (!loggedIn) {
            System.out.println("Login failed. Exiting.");
            return;
        }
        int choice = -1;
        while (choice != 0) {
            showMenu(isAdmin);
            System.out.print("Enter your choice: ");
            try {
                choice = sc.nextInt();
                sc.nextLine();

                if (isAdmin) {
                    if (choice == 1)
                        supplierMenu(sc);
                    else if (choice == 2)
                        productMenu(sc);
                    else if (choice == 3)
                        inventoryMenu(sc);
                    else if (choice == 4)
                    	orderMenu(sc);
                    else if (choice == 5)
                        transportationMenu(sc); 
                    else if (choice == 6)
                        warehouseMenu(sc);
                    else if (choice == 0)
                        System.out.println("Exiting... Goodbye!");
                    else
                        System.out.println("Invalid choice.");
                } else {
                    if (choice == 1)
                        listProducts();
                    else if (choice == 2)
                        createOrder(sc, loggedInUser, false);
                    else if (choice == 3)
                        trackShipment(sc, loggedInUser);
                    else if (choice == 0)
                        System.out.println("Exiting... Goodbye!");
                    else
                        System.out.println("Invalid choice.");
                }

            } catch (SupplyChainException e) {
                System.out.println("Operation failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void showMenu(boolean isAdmin) {
        System.out.println("\n========= TechNex Supply Chain Optimization System =========");
        if (isAdmin) {
            System.out.println("1. Supplier Management");
            System.out.println("2. Product Management");
            System.out.println("3. Inventory Management");
            System.out.println("4. Order Management");
            System.out.println("5. Transportation Management"); 
            System.out.println("6. Warehouse Management");
        } else {
            System.out.println("1. View Products");
            System.out.println("2. Place Order");
            System.out.println("3. Track My Shipment");
        }
        System.out.println("0. Exit");
        System.out.println("=============================================================");
    }

    // ---------------- SUPPLIER MANAGEMENT ----------------
    private static void supplierMenu(Scanner sc) throws Exception {
        int choice = -1;
        while (choice != 5) { 
            System.out.println("\n========= Supplier Management =========");
            System.out.println("1. Add Supplier");
            System.out.println("2. View Supplier");
            System.out.println("3. Update Supplier"); 
            System.out.println("4. Delete Supplier"); 
            System.out.println("5. Back to Main Menu"); 
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 1) addSupplier(sc);
            else if (choice == 2) {
                System.out.println("\n========= Supplier Management =========");
            	System.out.println("1. View All Suppliers");
            	System.out.println("2. View Suppliers by ID");
                System.out.print("Enter your choice: ");
            	int sup = sc.nextInt();
            	sc.nextLine();
            	if(sup==1) viewSupplier();
            	if(sup==2) viewSupplier(sc);
            }
            else if (choice == 3) updateSupplier(sc); 
            else if (choice == 4) deleteSupplier(sc); 
            else if (choice == 5) System.out.println("Returning to Main Menu...");
            else System.out.println("Invalid choice.");
        }
    }
    
    
    private static void addSupplier(Scanner sc) throws Exception {
        System.out.print("Supplier Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Contact Person: ");
        String contact = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();
        Supplier s = supplierService.createSupplier(name, contact, email, phone);
        System.out.println("Supplier added successfully! ID: " + s.getSupplierId());
    }

    
    private static void updateSupplier(Scanner sc) throws Exception {
        System.out.print("Enter Supplier ID to update: ");
        String id = sc.nextLine().trim();
        Supplier s = supplierService.getById(id);
        
        if (s == null) {
            System.out.println("No supplier found with ID: " + id);
            return;
        }

        System.out.println("\n--- Updating Supplier ID: " + s.getSupplierId() + " ---");
        System.out.println("Leave field blank to keep current value.");
        
        System.out.print("New Name (" + s.getName() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) s.setName(name);

        System.out.print("New Contact Person (" + s.getContactPerson() + "): ");
        String contact = sc.nextLine().trim();
        if (!contact.isEmpty()) s.setContactPerson(contact);
        
        System.out.print("New Email (" + s.getEmail() + "): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) s.setEmail(email);

        System.out.print("New Phone (" + s.getPhone() + "): ");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) s.setPhone(phone);
        
        supplierService.updateSupplier(s);
        System.out.println("Supplier updated successfully!");
    }

    
    private static void deleteSupplier(Scanner sc) throws Exception {
        System.out.print("Enter Supplier ID to delete: ");
        String id = sc.nextLine().trim();
        
        boolean ok = supplierService.deleteSupplier(id); 
        
        if (ok) System.out.println("Supplier deleted successfully!");
        else System.out.println("No supplier found with ID: " + id + " to delete.");
    }

    
    private static void viewSupplier(Scanner sc) throws Exception {
        System.out.print("Enter Supplier ID: ");
        String id = sc.nextLine().trim();
        Supplier s = supplierService.getById(id);
        if (s == null) System.out.println("No supplier found.");
        else {
            System.out.println("ID: " + s.getSupplierId());
            System.out.println("Name: " + s.getName());
            System.out.println("Contact Person: " + s.getContactPerson()); 
            System.out.println("Email: " + s.getEmail());
            System.out.println("Phone: " + s.getPhone());
        }
    }

    
    private static void viewSupplier() throws Exception {
        List<Supplier> list = supplierService.listAll();
        if (list.isEmpty()) System.out.println("No suppliers found.");
        else {
            for (Supplier s : list)
                System.out.println(s);
        }
    }

    // ---------------- PRODUCT MANAGEMENT ----------------
    
    private static void productMenu(Scanner sc) throws Exception {
        int choice = -1;
        while (choice != 5) {
            System.out.println("\n========= Product Management =========");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View Product");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) addProduct(sc);
            else if (choice == 2) updateProduct(sc);
            else if (choice == 3) deleteProduct(sc);
            else if (choice == 4) {
            	System.out.println("\n========= Product Management =========");
            	System.out.println("1. View Product by ID");
                System.out.println("2. List All Products");
                System.out.print("Enter your choice: ");
                int ch = sc.nextInt();
                sc.nextLine();
            	if(ch==1) listProducts(sc);
            	if(ch==2) listProducts();
            }
            else if (choice == 5) System.out.println("Returning to Main Menu...");
            else System.out.println("Invalid choice.");
        }
    }

    private static void addProduct(Scanner sc) throws Exception {
        System.out.print("Product Name: ");
        String name = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Unit Price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Category: ");
        String cat = sc.nextLine();
        System.out.print("Supplier ID: ");
        String sid = sc.nextLine();
        Product p = productService.createProduct(name, desc, price, cat, sid);
        System.out.println("Product added! ID: " + p.getProductId());
    }

    private static void updateProduct(Scanner sc) throws Exception {
        System.out.print("Enter Product ID to update: ");
        String id = sc.nextLine().trim();
        Product p = productService.getById(id);
        if (p == null) {
            System.out.println("No product found.");
            return;
        }
        System.out.println("Leave blank to keep old value.");
        System.out.print("New Name (" + p.getName() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) p.setName(name);
        System.out.print("New Description (" + p.getDescription() + "): ");
        String desc = sc.nextLine().trim();
        if (!desc.isEmpty()) p.setDescription(desc);
        System.out.print("New Price (" + p.getUnitPrice() + "): ");
        String priceStr = sc.nextLine().trim();
        if (!priceStr.isEmpty()) p.setUnitPrice(Double.parseDouble(priceStr));
        System.out.print("New Category (" + p.getCategory() + "): ");
        String cat = sc.nextLine().trim();
        if (!cat.isEmpty()) p.setCategory(cat);
        System.out.print("New Supplier ID (" + p.getSupplierId() + "): ");
        String sid = sc.nextLine().trim();
        if (!sid.isEmpty()) p.setSupplierId(sid);
        productService.updateProduct(p);
        System.out.println("Product updated successfully!");
    }

    private static void deleteProduct(Scanner sc) throws Exception {
        System.out.print("Enter Product ID to delete: ");
        String id = sc.nextLine().trim();
        boolean ok = productService.deleteProduct(id);
        if (ok) System.out.println("Product deleted.");
        else System.out.println("No product found with that ID.");
    }

    private static void listProducts(Scanner sc) throws Exception {
        System.out.print("Enter Product ID: ");
        String id = sc.nextLine().trim();
        Product p = productService.getById(id);
        if (p == null) System.out.println("No product found.");
        else {
            System.out.println("ID: " + p.getProductId());
            System.out.println("Name: " + p.getName());
            System.out.println("Desc: " + p.getDescription());
            System.out.println("Price: " + p.getUnitPrice());
            System.out.println("Category: " + p.getCategory());
            System.out.println("SupplierID: " + p.getSupplierId());
        }
    }

    private static void listProducts() throws Exception {
        List<Product> list = productService.listAll();
        if (list == null || list.isEmpty()) System.out.println("No products found.");
        else {
        	System.out.println("----------------------------");
        	System.out.println("     Product Details");
        	System.out.print("----------------------------");     
        	for (Product p : list) System.out.print(p);
        }
            
    }


  // ---------------- INVENTORY MANAGEMENT ----------------
     
     private static void inventoryMenu(Scanner sc) throws Exception {
         int choice = -1;
         while (choice != 5) {
             System.out.println("\n========= Inventory Management =========");
             System.out.println("1. Add Inventory");
             System.out.println("2. Update Inventory");
             System.out.println("3. Delete Inventory");
             System.out.println("4. View Inventory");
             System.out.println("5. Back to Main Menu");
             System.out.print("Enter your choice: ");
             choice = sc.nextInt();
             sc.nextLine();

             if (choice == 1) addInventory(sc);
             else if (choice == 2) updateInventory(sc);
             else if (choice == 3) deleteInventory(sc);
             else if (choice == 4) {
            	 System.out.println("\n========= Inventory Management =========");
            	 System.out.println("1. View Inventory by ID");
            	 System.out.println("2. List All Inventory");
            	 System.out.print("Enter your choice: ");
            	 int ch = sc.nextInt();
            	 sc.nextLine();
            	 if(ch==1) viewInventory(sc);
            	 if(ch==2) viewInventory();
             }
             else if (choice == 5) System.out.println("Returning to Main Menu...");
             else System.out.println("Invalid choice.");
         }
     }

     private static void addInventory(Scanner sc) throws Exception {
         System.out.print("Product ID: ");
         String pid = sc.nextLine();
         System.out.print("Quantity: ");
         int qty = Integer.parseInt(sc.nextLine());
         System.out.print("Warehouse ID: ");
         String wid = sc.nextLine();
         // FIX: Changed from inventoryMgmt to inventoryService
         Inventory inv = inventoryService.addStock(pid, qty, wid);
         System.out.println("Inventory added! ID: " + inv.getInventoryId());
     }

     private static void updateInventory(Scanner sc) throws Exception {
         System.out.print("Enter Product ID for update: ");
         String pid = sc.nextLine().trim();
         System.out.print("Enter Warehouse ID for update: ");
         String wid = sc.nextLine().trim();
         
         Inventory inv = inventoryService.getInventoryByProduct(pid, wid);
         if (inv == null) {
             System.out.println("No inventory found.");
             return;
         }

         System.out.println("Leave blank to keep old value.");
         
         System.out.print("New Quantity (" + inv.getQuantityInStock() + "): ");
         String qtyStr = sc.nextLine().trim();
         
         int newQuantity = inv.getQuantityInStock();
         if (!qtyStr.isEmpty()) {
             newQuantity = Integer.parseInt(qtyStr);
         }

         
         inventoryService.updateQuantity(pid, wid, newQuantity);
         
         System.out.println("Inventory updated successfully!");
     }

     private static void deleteInventory(Scanner sc) throws Exception {
         System.out.print("Enter Inventory ID to delete: ");
         String id = sc.nextLine().trim();
         
         
         boolean ok = inventoryService.deleteInventory(id); 
         if (ok) System.out.println("Inventory deleted.");
         else System.out.println("No inventory found with that ID.");
     }

     private static void viewInventory(Scanner sc) throws Exception {
         System.out.print("Enter Inventory ID: ");
         String id = sc.nextLine().trim();
         
         
         Inventory inv = inventoryService.getById(id);
         if (inv == null) System.out.println("No inventory found.");
         else {
             System.out.println("ID: " + inv.getInventoryId());
             System.out.println("Product ID: " + inv.getProductId());
             System.out.println("Quantity: " + inv.getQuantityInStock());
             System.out.println("Warehouse ID: " + inv.getWarehouseId());
         }
     }

     private static void viewInventory() throws Exception {
         List<Inventory> list = inventoryService.listAll();
         if (list == null || list.isEmpty()) System.out.println("No inventory found.");
         else for (Inventory inv : list)
             System.out.println(inv);
     }
    
  // ---------------- WAREHOUSE MANAGEMENT ----------------
     
     private static void warehouseMenu(Scanner sc) throws Exception {
         int choice = -1;
         while (choice != 5) { // Changed condition to '!= 5'
             System.out.println("\n========= Warehouse Management =========");
             System.out.println("1. Add Warehouse");
             System.out.println("2. View Warehouse");
             System.out.println("3. Delete Warehouse");
             System.out.println("4. Update Warehouse"); // New Option
             System.out.println("5. Back to Main Menu"); // Changed option number
             System.out.print("Enter choice: ");
             
             // Handle non-integer input for choice gracefully
             try {
                 choice = Integer.parseInt(sc.nextLine());
             } catch (NumberFormatException e) {
                 System.out.println("Invalid input. Please enter a number.");
                 continue; // Go back to the start of the while loop
             }

             if (choice == 1) addWarehouse(sc);
             else if (choice == 2) {
             	System.out.println("\n========= View Warehouse =========");
             	System.out.println("1. View Warehouse by ID");
                 System.out.println("2. List all Warehouse");
                 System.out.print("Enter choice: ");
                 int ch;
                 try {
                     ch = Integer.parseInt(sc.nextLine()); // Use nextLine() for consistency
                 } catch (NumberFormatException e) {
                     System.out.println("Invalid input. Please enter a number.");
                     continue; 
                 }
                 
                 if(ch==1) listWarehouses(sc);
                 else if(ch==2) listWarehouses();
                 else System.out.println("Invalid choice for View.");
             }
             else if (choice == 3) deleteWarehouse(sc); 
             else if (choice == 4) updateWarehouse(sc); // New method call
             else if (choice == 5) System.out.println("Returning to Main Menu..."); 
             else System.out.println("Invalid choice.");
         }
     }


     private static void updateWarehouse(Scanner sc) throws Exception {
         System.out.print("Enter Warehouse ID to update: ");
         String id = sc.nextLine().trim();

         Warehouse existing = warehouseService.getById(id);
         if (existing == null) {
             System.out.println("No warehouse found with ID: " + id);
             return;
         }

         System.out.println("Leave blank to keep old value.");
         System.out.print("Enter new Warehouse Name (" + existing.getName() + "): ");
         String newName = sc.nextLine();
         if (newName.trim().isEmpty()) newName = existing.getName();

         System.out.print("Enter new Location (" + existing.getLocation() + "): ");
         String newLocation = sc.nextLine();
         if (newLocation.trim().isEmpty()) newLocation = existing.getLocation();

         int newCapacity = existing.getCapacity();
         System.out.print("Enter new Capacity (" + existing.getCapacity() + "): ");
         String capacityInput = sc.nextLine();
         if (!capacityInput.trim().isEmpty()) {
             try {
                 newCapacity = Integer.parseInt(capacityInput);
             } catch (NumberFormatException e) {
                 System.out.println("Invalid capacity input. Keeping old capacity.");
             }
         }
         
         // Note: Used Capacity is not updated via the UI for simplicity, it should be managed internally
         // based on inventory (which is not part of the current code).

         Warehouse updated = warehouseService.updateWarehouse(id, newName, newLocation, newCapacity);

         if (updated != null) {
             System.out.println("Warehouse updated successfully!");
             System.out.println(updated);
         } else {
             System.out.println("Update failed for Warehouse ID: " + id);
         }
     }
     
     // ... (other UI methods remain the same) ...

    private static void addWarehouse(Scanner sc) throws Exception {
        System.out.print("Warehouse Name: ");
        String name = sc.nextLine();
        System.out.print("Location: ");
        String loc = sc.nextLine();
        System.out.print("Capacity: ");
        int capacity = Integer.parseInt(sc.nextLine());
        Warehouse w = warehouseService.addWarehouse(name, loc, capacity);
        System.out.println("Warehouse added! ID: " + w.getWarehouseId());
    }

    private static void listWarehouses() throws Exception {
        List<Warehouse> list = warehouseService.listAll();
        if (list == null || list.isEmpty()) System.out.println("No warehouses found.");
        else for (Warehouse w : list)
            System.out.println(w);
    }

    private static void listWarehouses(Scanner sc) throws Exception {
        System.out.print("Enter Warehouse ID: ");
        String id = sc.nextLine();
        Warehouse w = warehouseService.getById(id);
        if (w == null) System.out.println("No warehouse found.");
        else {
            System.out.println("ID: " + w.getWarehouseId());
            System.out.println("Name: " + w.getName());
            System.out.println("Location: " + w.getLocation());
            System.out.println("Capacity: " + w.getCapacity());
        }
    }

    private static void deleteWarehouse(Scanner sc) throws Exception {
        System.out.print("Enter Warehouse ID to delete: ");
        String id = sc.nextLine().trim();
        boolean ok = warehouseService.deleteWarehouse(id); 
        if (ok) System.out.println("Warehouse deleted.");
        else System.out.println("No warehouse found with that ID or deletion failed.");
    }
    
    // ---------------- TRANSPORTATION MANAGEMENT  -------------------
    
    private static void transportationMenu(Scanner sc) throws Exception {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n========= Transportation Management =========");
            System.out.println("1. Create Shipment (for an existing order)");
            System.out.println("2. Update Shipment Status");
            System.out.println("3. View Shipment");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 1) createShipment(sc);
            else if (choice == 2) updateShipmentStatus(sc);
            else if (choice == 3) {
            	System.out.println("\n========= Transportation Management =========");
            	System.out.println("1. View Shipment by ID");
                System.out.println("2. View All Shipments");
                System.out.print("Enter choice: ");
                int ch = sc.nextInt();
                sc.nextLine();
                if(ch==1) viewShipment(sc);
            	if(ch==2) viewShipment();
            }
            else if (choice == 4) System.out.println("Returning to Main Menu...");
            else System.out.println("Invalid choice.");
        }
    }

    private static void viewShipment(Scanner sc) throws Exception {
        System.out.print("Enter Shipment ID: ");
        String sid = sc.nextLine().trim();
        Transportation t = transService.getById(sid);
        if (t == null) {
            System.out.println("No shipment found with ID: " + sid);
            return;
        }

        System.out.println("\n--- Shipment Details ---");
        System.out.println("Shipment ID: " + t.getShipmentId());
        System.out.println("Order ID   : " + t.getOrderId());
        System.out.println("Carrier ID : " + t.getCarrierId());
        System.out.println("Status     : " + t.getStatus());
        System.out.println("Shipped On : " + t.getShippedDate());
    }


 // ---------------- ORDERS AND SHIPMENTS ----------------
    

	 private static void orderMenu(Scanner sc) throws Exception {
	     int choice = -1;
	     while (choice != 3) {
	         System.out.println("\n============ Order Management ============");
	         System.out.println("1. Create New Order (Admin)");
	         System.out.println("2. View Order");
	         System.out.println("3. Back to Main Menu");
	         System.out.print("Enter choice: ");
	         try {
	             choice = Integer.parseInt(sc.nextLine().trim());
	         } catch (NumberFormatException e) {
	             System.out.println("Invalid input. Please enter a number.");
	             continue;
	         }
	
	         if (choice == 1) createOrder(sc, loggedInUser, true); 
	         else if (choice == 2) {
	        	 System.out.println("\n============ Order Management ============");
	        	 System.out.println("1. View Order by ID");
		         System.out.println("2. View All Orders");
		         System.out.print("Enter choice: ");
		         int ch = sc.nextInt();
		         sc.nextLine();
		         if(ch==1) viewOrder(sc);
	        	 if(ch==2) viewOrder();
	         }
	         else if (choice == 3) System.out.println("Returning to Main Menu...");
	         else System.out.println("Invalid choice.");
	     }
	 }
	
	 private static void viewOrder(Scanner sc) throws Exception {
	     System.out.print("Enter Order ID: ");
	     String oid = sc.nextLine().trim();
	     Order o = orderService.getOrderById(oid); 
	
	     if (o == null) {
	         System.out.println("No order found with ID: " + oid);
	         return;
	     }
	
	     System.out.println("\n------ Order Details ------");
	     System.out.println("Order ID   : " + o.getOrderId());
	     System.out.println("Customer ID: " + o.getCustomerId());
	     System.out.println("Date       : " + o.getOrderDate());
	     System.out.println("Status     : " + o.getStatus());
	 }
	
	 private static void viewOrder() throws Exception {
	     List<Order> allOrders = orderService.listAllOrders(); 
	
	     if (allOrders == null || allOrders.isEmpty()) {
	         System.out.println("No orders found in the system.");
	         return;
	     }
	
	     System.out.println("\n============ All Orders ============");
	
	     for (Order o : allOrders) {
	         System.out.println(o);
	     }
	     System.out.println("-------------------------------------------------");
	 }
    
    
    private static void createOrder(Scanner sc, String user, boolean admin) throws Exception {
        
        System.out.println("\n--- Available Products ---");
        listProducts(); 
        System.out.println("--------------------------");

        System.out.print("Enter Product ID to order: ");
        String pid = sc.nextLine().trim();

        Product p = productService.getById(pid);
        if (p == null) {
            System.out.println("Product not found with ID: " + pid);
            return;
        }

        System.out.print("Enter Quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity entered. Order cancelled.");
            return;
        }

        if (qty <= 0) {
            System.out.println("Quantity must be positive. Order cancelled.");
            return;
        }

        List<Inventory> allInventory = inventoryService.listAll();
        int availableStock = 0;
        
        List<Inventory> fulfillmentCandidates = new java.util.ArrayList<>();
        
        for (Inventory inv : allInventory) {
            if (inv.getProductId().equals(pid)) {
                availableStock += inv.getQuantityInStock();
                fulfillmentCandidates.add(inv);
            }
        }
        
        if (qty > availableStock) {
            System.out.println("Insufficient inventory.");
            System.out.println("Requested: " + qty + ", Available: " + availableStock);
            System.out.println("Order cancelled.");
            return;
        }
        
        double totalAmount = p.getUnitPrice() * qty;
        
        System.out.printf("TOTAL AMOUNT:   ₹%.2f\n", totalAmount);
        
        System.out.print("Confirm and create order? (yes/no): ");
        String finalConfirm = sc.nextLine().trim().toLowerCase();

        if (finalConfirm.equals("yes")) {
            int remainingNeeded = qty;
            try {
                for (Inventory inv : fulfillmentCandidates) {
                    if (remainingNeeded <= 0) break; 
                    int available = inv.getQuantityInStock();
                    int amountToTake = Math.min(remainingNeeded, available);
                    
                    if (amountToTake > 0) {
                        inventoryService.deductStock(pid, amountToTake, inv.getWarehouseId());
                        remainingNeeded -= amountToTake;
//                        System.out.printf("   > Deducted %d units from Warehouse %s.\n", amountToTake, inv.getWarehouseId());
                    }
                }
                
                if (remainingNeeded > 0) {
                    throw new SupplyChainException("Fulfillment Error: Stock deduction failed to complete.");
                }
//                System.out.println("Inventory successfully deducted.");
            } catch (SupplyChainException e) {
                System.out.println("CRITICAL FULFILLMENT ERROR: " + e.getMessage());
                System.out.println("Order creation failed due to stock deduction error.");
                return;
            }

            Order o = orderService.createOrder(user, totalAmount);
            System.out.println("Order created! Order ID: " + o.getOrderId());

            String generatedShipmentId = null;
            
            if (admin) {
                System.out.print("Assign shipment now? (yes/no): ");
                String ans = sc.nextLine().trim().toLowerCase();
                if (ans.equals("yes")) {
                    Transportation shipment = createShipmentForOrderAndReturn(sc, o.getOrderId());
                    if (shipment != null) {
                        generatedShipmentId = shipment.getShipmentId();
                    }
                }
            }
            
            if (generatedShipmentId != null) {
                System.out.println("Shipment ID: " + generatedShipmentId + ". Use this ID to track your order.");
            } else if (!admin) {
                 System.out.println("Shipment will be assigned and tracked by an Admin shortly.");
            }
        } else {
            System.out.println("Order cancelled by user.");
        }
    }
    
    private static void updateOrderStatus(Scanner sc) throws Exception {
        System.out.println("\n========= Update Order Status =========");
        
        System.out.print("Enter Order ID to update: ");
        String orderId = sc.nextLine().trim();

        // 1. Check if the order exists
        Order o = orderService.getOrderById(orderId);
        if (o == null) {
            System.out.println("No order found with ID: " + orderId);
            return;
        }

        System.out.println("Current Status: " + o.getStatus());
        
        // Define allowed statuses for the user
        String allowedStatuses = "New, Confirmed, Cancelled, Completed, Shipped";
        System.out.println("Allowed new statuses: [" + allowedStatuses + "]");
        
        System.out.print("Enter NEW Status: ");
        String newStatus = sc.nextLine().trim();

        try {
            // 2. Call the service layer to perform the update and validation
            orderService.updateOrderStatus(orderId, newStatus);
            System.out.println("\n✅ Order ID " + orderId + " successfully updated to: " + newStatus + ".");
        } catch (SupplyChainException e) {
            // This catches the validation errors (e.g., status not allowed, or DB errors)
            System.out.println("Update failed: " + e.getMessage());
        }
    }
    
    private static void createShipment(Scanner sc) throws Exception {
        System.out.print("Enter Order ID for shipment: ");
        String orderId = sc.nextLine().trim();

        createShipmentForOrder(sc, orderId);
    }


    private static void createShipmentForOrder(Scanner sc, String orderId) throws Exception {
        createShipmentForOrderAndReturn(sc, orderId);
    }
    
    private static Transportation createShipmentForOrderAndReturn(Scanner sc, String orderId) throws Exception {
        System.out.print("Carrier ID (leave blank for auto-generation): ");
        String carrierId = sc.nextLine().trim();
        if (carrierId.isEmpty()) carrierId = null;

        System.out.print("Shipment Status (leave blank for default 'Shipped'): ");
        String status = sc.nextLine().trim();
        if (status.isEmpty()) status = null;

        Transportation shipment = transService.createShipment(orderId, carrierId, status);
        System.out.println("Shipment created! Shipment ID: " + shipment.getShipmentId());
        return shipment;
    }


    private static void updateShipmentStatus(Scanner sc) throws Exception {
        System.out.print("Shipment ID: ");
        String sid = sc.nextLine();
        System.out.print("New Status (Pending/Shipped/Delivered): ");
        String status = sc.nextLine();
        transService.updateStatus(sid, status);
        System.out.println("Shipment status updated.");
    }

    private static void viewShipment() throws Exception {
        List<Transportation> list = transService.listAll(); 
        
        if (list == null || list.isEmpty()) {
            System.out.println("No shipments found.");
        } else {
            System.out.println("ShipmentID | OrderID | CarrierID | Status | ShippedDate");
            System.out.println("----------------------------------------------------------");
            for (Transportation t : list) {
                System.out.println(
                    t.getShipmentId() + " | " +
                    t.getOrderId() + " | " +
                    t.getCarrierId() + " | " +
                    t.getStatus() + " | " +
                    t.getShippedDate()
                );
            }
        }
    }


    private static void trackShipment(Scanner sc, String user) throws Exception {
        System.out.println("\n--- Tracking Shipments for User: " + user + " ---");
        
        // 1. Get all orders placed by the current user
        List<Order> userOrders = orderService.getByCustomer(user); // **Assumption: orderMgmt has getByCustomer(String user)**

        if (userOrders == null || userOrders.isEmpty()) {
            System.out.println("No orders found to track.");
            return;
        }

        System.out.println("Order ID | Shipment ID | Shipment Status");
        System.out.println("-----------------------------------------------------------------");
        
        boolean foundShipment = false;

        for (Order o : userOrders) {
            
            
            Transportation t = transService.getByOrderId(o.getOrderId()); 
            
            String shipmentId = "N/A";
            String shipmentStatus = "Not Assigned";
            
            if (t != null) {
                shipmentId = t.getShipmentId();
                shipmentStatus = t.getStatus();
                foundShipment = true;
            }

            System.out.printf("%s | %s | %s\n", 
                o.getOrderId(), 
                shipmentId, 
                shipmentStatus
            );
        }

        if (!foundShipment) {
            System.out.println("No shipments are currently assigned to your orders.");
        }
    }
}