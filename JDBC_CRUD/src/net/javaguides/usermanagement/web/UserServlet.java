package net.javaguides.usermanagement.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.net.javaguides.usermanagement.modelment.model.User;
import src.main.java.net.javaguides.usermanagement.dao.UserDAO;



/**
 * ControllerServlet.java
 * This servlet acts as a page controller for the application, handling all
 * requests from the user.
 * @email Ramesh Fadatare
 */

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	
	public void init() {
		userDAO = new UserDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action.toString() + "hiiiiii");

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/search":
				showSearchForm(request, response);
				break;
			case "/searchUser":
				showSearchUser(request, response);
				break;
			case "/insert":
				insertUser(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			case "/login":
				loginUser(request, response);
				break;
			case "/logout":
				logoutUser(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<User> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
	private void showSearchForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-search.jsp");
		dispatcher.forward(request, response);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		userDAO.insertUser(newUser);
		response.sendRedirect("list");
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);

	}

	private void showSearchUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		String userName="";
		userName=request.getParameter("name");
		List<User> listUser = userDAO.searchUser(userName);
		request.setAttribute("searchUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-searchDB.jsp");
		dispatcher.forward(request, response);
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		User book = new User(id, name, email, country);
		userDAO.updateUser(book);
		response.sendRedirect("list");
	}
	private void loginUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
	       
		// Set the content type of response to "text/html"
	        response.setContentType("text/html");
	        
	        // Get the print writer object to write into the response
	        PrintWriter out = response.getWriter();
	  
	        // Get the session object
	        HttpSession session = request.getSession();
	  
	        // Get User entered details from the request using request parameter.
	        String user = request.getParameter("usName");
	        String password = request.getParameter("usPass");
	  
	        // Validate the password - If password is correct, 
	        // set the user in this session
	        // and redirect to welcome page
	        if (password.equals("geek")) {
	            session.setAttribute("user", user);
	           // response.sendRedirect("welcome.jsp?name=" + user);
	            RequestDispatcher rd = request.getRequestDispatcher("user-list.jsp");

	        }
	        // If the password is wrong, display the error message on the login page.
	        else {
	            RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
	            out.println("<font color=red>Password is wrong.</font>");
	            rd.include(request, response);
	        }
	        // Close the print writer object.
	        out.close();
	    
	}
	private void logoutUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		response.setContentType("text/html");  
		PrintWriter out=response.getWriter();  
System.out.println("hi---------");
		try {
			request.getRequestDispatcher("link.html").include(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		HttpSession session=request.getSession();  
		session.invalidate();  

		out.print("You are successfully logged out!");  

		out.close();  
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("list");

	}

}
