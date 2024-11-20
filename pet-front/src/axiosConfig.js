import axios from 'axios';

// Set the base URL for Axios
axios.defaults.baseURL = process.env.REACT_APP_API_URL;

// Add the request interceptor
axios.interceptors.request.use(
  (config) => {
    // Retrieve the token from sessionStorage
    const token = sessionStorage.getItem('token');
    if (token) {
      // Add Authorization header if the token exists
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config; // Continue with the request
  },
  (error) => {
    // Handle request errors
    console.error('Request error:', error);
    return Promise.reject(error); // Reject the request error
  }
);

// Add the response interceptor
axios.interceptors.response.use(
  (response) => {
    // Handle a successful response (status codes 200-299)
    console.log('Response received:', response);
    return response; // Pass the response to the calling code
  },
  (error) => {
    // Handle error responses (status codes 4xx, 5xx)
    if (error.response) {
      if (error.response.status === 401) {
        // If unauthorized, redirect to login and clear session storage
        console.warn('Unauthorized - redirecting to login');
        sessionStorage.clear(); // Clear session data
        window.location.href = '/unauthorized'; // Redirect to login
      } else if (error.response.status === 500) {
        console.error('Server error:', error.response.data);
      } else {
        console.error('API error:', error.response.data || error.message);
      }
    } else {
      // Handle network or unknown errors
      console.error('Network or unknown error:', error.message);
    }
    return Promise.reject(error); // Reject the error to propagate it
  }
);

export default axios;
