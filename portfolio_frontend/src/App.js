import './App.css';
import React, { useState, useEffect } from 'react';
import ApiService from './ApiService';
import Login from './Login';
import PortfolioDetails from './PortfolioDetails';

function App() {
  const [portfolios, setPortfolios] = useState([]);
  const [userPortfolio, setUserPortfolio] = useState(null);
  const [portfolioDetails, setPortfolioDetails] = useState({});
  const [accessToken, setAccessToken] = useState('');
  const [refreshToken, setRefreshToken] = useState('');
  const [refreshTokenExpiryDate, setRefreshTokenExpiryDate] = useState(null);
  const [accessTokenExpiryDate, setAccessTokenExpiryDate] = useState(null);
  const [activeTab, setActiveTab] = useState(null);
  const [accessCodes, setAccessCodes] = useState({});
  const [showLogin, setShowLogin] = useState(false);
  const [userAccessCode, setUserAccessCode] = useState('');

  useEffect(() => {
    const fetchDataForLoggedInUser = async () => {
      try {
        await loadPortfolios(); 
        await loadUserPortfolio();
        await loadAccessCode(); 
      } catch (error) {
        alert('Fehler beim Laden der Portfolios für eingeloggten User:', error);
      }
    };
  
    const fetchPortfoliosOnly = async () => {
      try {
        await loadPortfolios(); 
      } catch (error) {
        alert('Fehler beim Laden der Portfolios:', error);
      }
    };

    const initializeApp = async () => {
      const storedAccessToken = localStorage.getItem('accessToken');
      const storedAccessTokenExpiryDate = localStorage.getItem('accessTokenExpiryDate');
      const storedRefreshToken = localStorage.getItem('refreshToken');
      const storedRefreshTokenExpiryDate = localStorage.getItem('refreshTokenExpiryDate');
  
      if (storedAccessToken && storedAccessTokenExpiryDate && storedRefreshToken && storedRefreshTokenExpiryDate) {
        setAccessToken(storedAccessToken);
        setAccessTokenExpiryDate(new Date(storedAccessTokenExpiryDate));
        setRefreshToken(storedRefreshToken);
        setRefreshTokenExpiryDate(new Date(storedRefreshTokenExpiryDate));
  
        const now = new Date();
        if (new Date(storedAccessTokenExpiryDate) <= now) {
          try {
            await refreshAccessToken(storedRefreshToken); 
            await fetchDataForLoggedInUser(); 
          } catch (error) {
            console.error('Error during token refresh or data fetching:', error);
          }
        } else {
          await fetchDataForLoggedInUser(); 
        }
      } else {
        await fetchPortfoliosOnly(); 
      }
    };

    initializeApp();

  }, []);

  useEffect(() => {
    if (portfolios.length > 0) {
      if (accessToken && userPortfolio) {
        setActiveTab('userPortfolio');
      } else {
        setActiveTab(portfolios[0].portfolioId);
      }
    }
  }, [portfolios, accessToken, userPortfolio]);

  useEffect(() => {
    if (refreshToken && refreshTokenExpiryDate && accessToken && accessTokenExpiryDate) {
      const interval = setInterval(() => {
        const now = new Date();
        const timeUntilAccessTokenExpiry = new Date(accessTokenExpiryDate) - now;
        const timeUntilRefreshTokenExpiry = new Date(refreshTokenExpiryDate) - now;

        if (timeUntilAccessTokenExpiry <= 300000) { // 5 Minuten vorher
          handleTokenRefresh();
        }

        if (timeUntilRefreshTokenExpiry <= 300000) { // 5 Minuten vorher
          handleLogout();
        }
      }, 60000); // Überprüfung jede Minute

      return () => clearInterval(interval);
    }
  }, [refreshToken, refreshTokenExpiryDate, accessTokenExpiryDate]);

  const refreshAccessToken = (token) => {
    ApiService.refreshToken(token)
      .then(data => {
        setAccessToken(data.tokens.accessToken);
        setRefreshToken(data.tokens.refreshToken);
        setAccessTokenExpiryDate(new Date(data.tokens.accessTokenExpiryDate));
        setRefreshTokenExpiryDate(new Date(data.tokens.refreshTokenExpiryDate));
        localStorage.setItem('accessToken', data.tokens.accessToken);
        localStorage.setItem('accessTokenExpiryDate', data.tokens.accessTokenExpiryDate);

        return Promise.all([loadUserPortfolio(), loadPortfolios(), loadAccessCode()]);
      })
      .catch(error => {
        console.error('Error refreshing token', error);
        handleLogout();
      });
  };

  const loadPortfolios = () => {
    ApiService.getPortfolios()
      .then(portfolios => setPortfolios(portfolios))
      .catch(error => console.error('Error fetching portfolios', error));
  };

  const loadUserPortfolio = () => {
    ApiService.getPortfolioByUserId()
      .then(response => {
        const portfolio = response;
        setUserPortfolio(portfolio);
        setPortfolioDetails(prevDetails => ({
          ...prevDetails,
          [portfolio.portfolioId]: portfolio
        }));
      })
      .catch(error => {
        console.error('Error fetching user portfolio', error);
        //alert('Fehler beim Laden des Benutzerportfolios!');
      });
  };

  const handleTokenRefresh = () => {
    const storedRefreshToken = localStorage.getItem('refreshToken');
    ApiService.refreshToken(storedRefreshToken)
      .then(data => {
        setAccessToken(data.tokens.accessToken);
        setAccessTokenExpiryDate(new Date(data.tokens.accessTokenExpiryDate));
        localStorage.setItem('accessToken', data.tokens.accessToken);
        localStorage.setItem('accessTokenExpiryDate', data.tokens.accessTokenExpiryDate);
      })
      .catch(error => {
        console.error('Error refreshing token', error);
        handleLogout();
      });
  };

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('accessTokenExpiryDate');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('refreshTokenExpiryDate');
    window.location.reload()
  };

  const handleLoginSuccess = (data) => {
    setAccessToken(data.tokens.accessToken);
    setRefreshToken(data.tokens.refreshToken);
    setAccessTokenExpiryDate(new Date(data.tokens.accessTokenExpiryDate));
    setRefreshTokenExpiryDate(new Date(data.tokens.refreshTokenExpiryDate));
    localStorage.setItem('accessToken', data.tokens.accessToken);
    localStorage.setItem('accessTokenExpiryDate', data.tokens.accessTokenExpiryDate);
    localStorage.setItem('refreshToken', data.tokens.refreshToken);
    localStorage.setItem('refreshTokenExpiryDate', data.tokens.refreshTokenExpiryDate);

    loadUserPortfolio();
    loadAccessCode();
    setShowLogin(false);
  };

  const getPortfolioDetails = (portfolioId) => {
    const accessCode = accessCodes[portfolioId];
    ApiService.getPortfolioDetails(portfolioId, accessCode)
      .then(portfolioDetails => {
        setPortfolioDetails(prevDetails => ({
          ...prevDetails,
          [portfolioId]: portfolioDetails
        }));
        setActiveTab(portfolioId); // Set the active tab to the selected portfolio
      })
      .catch(error => {
        console.error('Error fetching portfolio details', error);
        alert('Falscher Zugangscode oder technischer Fehler!');
      });
  };

  const handleAccessCodeChange = (event, portfolioId) => {
    const newAccessCodes = { ...accessCodes, [portfolioId]: event.target.value };
    setAccessCodes(newAccessCodes);
  };

  const loadAccessCode = () => {
    ApiService.getAccessCode()
      .then(response => {
        setUserAccessCode(response);
      })
      .catch(error => {
        console.error('Error fetching access code', error);
        //alert('Fehler beim Laden des Zugangscodes!');
      });
  };

  const handleFormSubmit = (portfolioId) => {
    if (accessCodes[portfolioId] && accessCodes[portfolioId].length === 6) {
      getPortfolioDetails(portfolioId);
    } else {
      alert('Bitte geben Sie einen 6-stelligen Zugangscode ein.');
    }
  };

  const updatePortfolioAssetQuantity = (portfolioAssetId, quantityChange) => {
    ApiService.updatePortfolioAssetQuantity(portfolioAssetId, quantityChange)
      .then(response => {
        alert('Asset erfolgreich aktualisiert!');
        // Portfolio nach der Aktualisierung neu laden
        loadUserPortfolio();
        loadPortfolios();
      })
      .catch(error => {
        console.error('Error updating portfolio asset', error);
        alert('Fehler beim Aktualisieren des Assets!');
      });
  };

  const renderTabContent = (portfolio) => {
    const details = portfolioDetails[portfolio.portfolioId];
    if (details) {
      return (
        <PortfolioDetails
          portfolioAssets={details.portfolioAssets || []}
          updatePortfolioAssetQuantity={updatePortfolioAssetQuantity}
          isUserPortfolio={portfolio.portfolioId === userPortfolio?.portfolioId}
          portfolioId={portfolio.portfolioId}
        />
      );
    }

    // Fake data for the blurred chart
    const fakePortfolio = {
      portfolioAssets: [
        { asset: { name: 'Asset 1', lastValue: 100 }, quantity: 10 },
        { asset: { name: 'Asset 2', lastValue: 100 }, quantity: 5 },
        { asset: { name: 'Asset 3', lastValue: 100 }, quantity: 2 },
        { asset: { name: 'Asset 4', lastValue: 100 }, quantity: 1 },
        { asset: { name: 'Asset 5', lastValue: 100 }, quantity: 1 },
        { asset: { name: 'Asset 6', lastValue: 100 }, quantity: 1 }
      ]
    };

    return (
      <div className="blurred-portfolio-container">
        <div className="blurred-portfolio">
          <PortfolioDetails
            portfolioAssets={fakePortfolio.portfolioAssets}
            updatePortfolioAssetQuantity={() => {}}
            isUserPortfolio={false}
          />
        </div>
        <div className="access-code-container">
          <input
            type="text"
            placeholder="Zugangscode"
            onChange={(e) => handleAccessCodeChange(e, portfolio.portfolioId)}
          />
          <button onClick={() => handleFormSubmit(portfolio.portfolioId)}>Details anzeigen</button>
        </div>
      </div>
    );
    };





  return (
    <div className="App">
      <header className="App-header">
        <h1>Portfolio Manager</h1>
        <div className="auth-buttons">
          {accessToken ? (
            <button onClick={handleLogout}>Logout</button>
          ) : (
            <button onClick={() => setShowLogin(true)}>Login</button>
          )}
        </div>
      </header>
      <main>
        {showLogin && !accessToken && (
          <div className="overlay">
            <div className="login-modal">
              <Login onLoginSuccess={handleLoginSuccess} onCancel={() => setShowLogin(false)} />
            </div>
          </div>
        )}
        {!accessToken && (
          <>
            <div>
              <h2>All Portfolios</h2>
              <div className="tabs">
                {portfolios.map(portfolio => (
                  <button
                    key={portfolio.portfolioId}
                    className={`tab-button ${activeTab === portfolio.portfolioId ? 'active' : ''}`}
                    onClick={() => setActiveTab(portfolio.portfolioId)}
                  >
                    {portfolio.name}
                  </button>
                ))}
              </div>
              {portfolios.map(portfolio => (
                activeTab === portfolio.portfolioId && (
                  <div key={portfolio.portfolioId} className="tab-content">
                    {renderTabContent(portfolio)}
                  </div>
                )
              ))}
            </div>
          </>
        )}
        {accessToken && (
          <div>
            <h2>Portfolios</h2>
            <div className="tabs">
              <button
                className={`tab-button ${activeTab === 'userPortfolio' ? 'active' : ''}`}
                onClick={() => setActiveTab('userPortfolio')}
              >
                Mein Portfolio
              </button>
              {portfolios
                .filter(portfolio => userPortfolio && portfolio.portfolioId !== userPortfolio.portfolioId)
                .map(portfolio => (
                  <button
                    key={portfolio.portfolioId}
                    className={`tab-button ${activeTab === portfolio.portfolioId ? 'active' : ''}`}
                    onClick={() => setActiveTab(portfolio.portfolioId)}
                  >
                    {portfolio.name}
                  </button>
                ))}
            </div>
            {activeTab === 'userPortfolio' && userPortfolio && (
              <div className="tab-content">
                <PortfolioDetails
                  portfolioAssets={userPortfolio.portfolioAssets}
                  updatePortfolioAssetQuantity={updatePortfolioAssetQuantity}
                  isUserPortfolio={true}
                  accessCode={userAccessCode}
                  portfolioId={userPortfolio.portfolioId}
                />
              </div>
            )}
            {portfolios.map(portfolio => (
              activeTab === portfolio.portfolioId && (
                <div key={portfolio.portfolioId} className="tab-content">
                  {renderTabContent(portfolio)}
                </div>
              )
            ))}
          </div>
        )}
      </main>
    </div>
  );
  
  
}

export default App;
