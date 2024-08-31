import React, { useState, useEffect } from 'react';
import ChartComponent from './Chart';
import ApiService from './ApiService';
import { FaEye, FaEyeSlash } from 'react-icons/fa';

const PortfolioDetails = ({ portfolioAssets, updatePortfolioAssetQuantity, isUserPortfolio, accessCode, portfolioId  }) => {
 
  const [newAccessCode, setNewAccessCode] = useState('');
  const [showAccessCode, setShowAccessCode] = useState(false);
  const [assetQuantities, setAssetQuantities] = useState({});

  useEffect(() => {
    setNewAccessCode(accessCode);
  }, [accessCode]);

  const handleAccessCodeChange = async () => {
    if (newAccessCode.length === 6) {
        try {
          await ApiService.updateAccessCode(newAccessCode, portfolioId);
          setShowAccessCode(false)
          alert('Zugangscode erfolgreich geändert!');
        } catch (error) {
          alert('Fehler beim Ändern des Zugangscodes!');
        }
      } else {
        alert('Der Zugangscode muss genau 6 Zeichen lang sein.');
      }
  };
 
  if (!portfolioAssets || !portfolioAssets) {
    return <div>Loading...</div>;
  }

  // Sortieren der Portfolio Assets nach dem Gesamtwert (absteigend)
  const sortedAssets = [...portfolioAssets].sort((a, b) => 
    (b.quantity * b.asset.lastValue) - (a.quantity * a.asset.lastValue)
  );

  const chartData = {
    labels: sortedAssets.map(asset => asset.asset.name),
    datasets: [{
      data: sortedAssets.map(asset => (asset.quantity * asset.asset.lastValue)),
      backgroundColor: ['#bf5285', '#5252bf', '#52bcbf', '#52bf6d', '#bfb852', '#bf6c52'],
      hoverBackgroundColor: ['#b8698d', '#6565bf', '#6fbabd', '#69bf7f', '#bab461','#bd755e'],
      sortedAssets: sortedAssets // Zusätzliche Informationen für custom
    }]
  };

  const toggleShowAccessCode = () => {
    setShowAccessCode(!showAccessCode);
  };

  const handleQuantityChange = (e, portfolioAssetId) => {
    const { value } = e.target;
    setAssetQuantities(prevQuantities => ({
      ...prevQuantities,
      [portfolioAssetId]: value
    }));
  };

  const handleUpdateQuantity = async (portfolioAssetId) => {
    const quantityChange = assetQuantities[portfolioAssetId];
    if (quantityChange) {
      await updatePortfolioAssetQuantity(portfolioAssetId, quantityChange);
      setAssetQuantities(prevQuantities => ({
        ...prevQuantities,
        [portfolioAssetId]: ''
      }));
    }
  };

  function formatAssetImageName(name) {
    return name.replace(/[^a-zA-Z0-9]/g, '_').toLowerCase();
  }

  return (
    <>
      <div className="portfolio-details">
        <ChartComponent type="Doughnut" data={chartData} />
      </div>

      {isUserPortfolio && (
        <div className='update-portfolio-assets'>
          <div className="update-assets">
            <h3>Update Portfolio Assets</h3>
            <ul>
              {portfolioAssets.map(asset => (
                <li key={asset.portfolioAssetId} className="asset-item">
                  <div className="asset-info">
                    <img src={`/${formatAssetImageName(asset.asset.name)}.png`} alt={asset.asset.name} className="asset-logo" />
                    <span className="asset-quantity">{asset.quantity.toFixed(5)}</span>
                  </div>
                  <input 
                    type="number" 
                    className="quantity-input" 
                    value={assetQuantities[asset.portfolioAssetId] || ''} 
                    onChange={(e) => handleQuantityChange(e, asset.portfolioAssetId)} 
                  />
                  <button 
                    className="update-button" 
                    onClick={() => handleUpdateQuantity(asset.portfolioAssetId)}
                  >
                    Update
                  </button>
                </li>
              ))}
            </ul>
          </div>
          <div className="access-code-section">
            <h3>Zugangscode ändern</h3>
            <div className="access-code-input-container">
              <input
                type={showAccessCode ? 'text' : 'password'}
                value={newAccessCode}
                onChange={(e) => setNewAccessCode(e.target.value)} 
              />
              <button className="toggle-visibility" onClick={toggleShowAccessCode}>
                {showAccessCode ? <FaEyeSlash /> : <FaEye />}
              </button>
              <button className="change-code-button" onClick={handleAccessCodeChange} disabled={newAccessCode.length !== 6}>
                Ändern
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default PortfolioDetails;
