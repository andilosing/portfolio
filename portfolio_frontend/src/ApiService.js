const API_URL =  "http://87.106.57.116:8080/api" //"http://158.101.172.148:8080/api"; //"http://localhost:8080/api"

class ApiService {
  static async getPortfolios() {
    try {
      const response = await fetch(`${API_URL}/portfolio/`);
      const data = await response.json();
      return data.portfolios;
    } catch (error) {
      console.error('Error fetching portfolios', error);
      throw error;
    }
  }

  static async getPortfolioDetails(portfolioId, accessCode) {
    try {
      const response = await fetch(`${API_URL}/portfolio/${portfolioId}/verify-access-code?accessCode=${accessCode}`);
      const data = await response.json();
      if(data.errorMessage == "invalid access code")
        alert("Ungültiger Zugriffstoken")
      return data.portfolioDetails;
    } catch (error) {
      console.error('Error fetching portfolio details', error);
      throw error;
    }
  }

  static async login(username, password) {
    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error logging in', error);
      throw error;
    }
  }

  static async refreshToken(refreshToken) {
    try {
      const response = await fetch(`${API_URL}/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken }),
      });
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error refreshing token', error);
      throw error;
    }
  }

  static async getPortfolioByUserId() {
    const response = await fetch(`${API_URL}/portfolio/owner`, {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` },
    });
    const data = await response.json();
    return data.portfolioDetails;
  }

  static async getAccessCode() {
    const response = await fetch(`${API_URL}/portfolio/access-code`, {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` },
    });
    return response.json();
  }

  static async updatePortfolioAssetQuantity(portfolioAssetId, quantityChange) {
    const response = await fetch(`${API_URL}/portfolio-asset/${portfolioAssetId}?quantityChange=${quantityChange}`, {
      method: 'PUT',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
        'Content-Type': 'application/json'
      }
    });
    return response.json();
  }

  static async updateAccessCode(newAccessCode, portfolioId) {

    const response = await fetch(`${API_URL}/portfolio/${portfolioId}/set-access-code?accessCode=${newAccessCode}`, {
      method: 'POST',
      headers: { 
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
        
      },
    });
   
    const data = await response.json();
    return data;
  }
}


export default ApiService;
