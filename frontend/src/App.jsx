import React, { useState } from 'react';
import axios from 'axios';
import { Search, Loader2, BookOpen, FileText, AlertCircle, Clock, BarChart3, GraduationCap } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell, PieChart, Pie, Legend } from 'recharts';
import './App.css';

function App() {
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState(null);
  const [error, setError] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setError(null);
    setResults(null);

    try {
      const response = await axios.post('http://localhost:8080/api/search/analyze', {
        query: query,
        maxResults: 10
      });
      setResults(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to analyze query. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const getCategoryColor = (category) => {
    const colors = {
      'Paper Structure': '#2563eb',
      'Research Content': '#7c3aed',
      'Technical Details': '#059669',
      'Analysis': '#dc2626',
      'Supporting': '#ea580c'
    };
    return colors[category] || '#6b7280';
  };

  const preparePieData = () => {
    if (!results?.subHeadings) return [];
    
    const categoryGroups = {};
    results.subHeadings.forEach(heading => {
      if (!categoryGroups[heading.category]) {
        categoryGroups[heading.category] = 0;
      }
      categoryGroups[heading.category] += heading.count;
    });

    return Object.entries(categoryGroups).map(([name, value]) => ({
      name,
      value,
      color: getCategoryColor(name)
    }));
  };

  return (
    <div className="app">
      <div className="paper-texture"></div>
      
      <div className="container">
        <header className="header">
          <div className="logo-section">
            <div className="logo-icon">
              <GraduationCap className="icon" />
            </div>
            <div>
              <h1 className="title">Scholar<span className="title-accent">Scan</span></h1>
              <p className="subtitle">Deep Learning Journal Paper Analysis</p>
            </div>
          </div>
        </header>

        <div className="search-section">
          <form onSubmit={handleSearch} className="search-form">
            <div className="search-input-wrapper">
              <Search className="search-icon" />
              <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search for deep learning papers (e.g., 'transformer architecture', 'CNN image classification')..."
                className="search-input"
                disabled={loading}
              />
            </div>
            <button 
              type="submit" 
              className="search-button"
              disabled={loading || !query.trim()}
            >
              {loading ? (
                <>
                  <Loader2 className="icon spin" />
                  Analyzing
                </>
              ) : (
                <>
                  <BookOpen className="icon" />
                  Analyze Papers
                </>
              )}
            </button>
          </form>

          {error && (
            <div className="error-message">
              <AlertCircle className="icon" />
              {error}
            </div>
          )}
        </div>

        {results && (
          <div className="results-section">
            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-icon blue">
                  <FileText className="icon" />
                </div>
                <div className="stat-content">
                  <div className="stat-label">Papers Found</div>
                  <div className="stat-value">{results.totalResults}</div>
                </div>
              </div>

              <div className="stat-card">
                <div className="stat-icon purple">
                  <BarChart3 className="icon" />
                </div>
                <div className="stat-content">
                  <div className="stat-label">Sub-Headings</div>
                  <div className="stat-value">{results.subHeadings.length}</div>
                </div>
              </div>

              <div className="stat-card">
                <div className="stat-icon orange">
                  <Clock className="icon" />
                </div>
                <div className="stat-content">
                  <div className="stat-label">Processing Time</div>
                  <div className="stat-value">{results.processingTimeMs}ms</div>
                </div>
              </div>
            </div>

            <div className="section-header">
              <h2 className="section-title">Sub-Heading Distribution</h2>
              <p className="section-description">
                Common sections found across {results.totalResults} deep learning papers
              </p>
            </div>

            <div className="charts-grid">
              <div className="chart-card">
                <h3 className="chart-title">Sub-Headings by Frequency</h3>
                <ResponsiveContainer width="100%" height={400}>
                  <BarChart data={results.subHeadings.slice(0, 10)}>
                    <CartesianGrid strokeDasharray="3 3" stroke="rgba(0,0,0,0.05)" />
                    <XAxis 
                      dataKey="name" 
                      angle={-45}
                      textAnchor="end"
                      height={120}
                      tick={{ fill: '#374151', fontSize: 12 }}
                    />
                    <YAxis tick={{ fill: '#374151' }} />
                    <Tooltip 
                      contentStyle={{ 
                        backgroundColor: 'rgba(255, 255, 255, 0.98)', 
                        border: '1px solid #e5e7eb',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
                      }}
                    />
                    <Bar dataKey="count" radius={[8, 8, 0, 0]}>
                      {results.subHeadings.slice(0, 10).map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={getCategoryColor(entry.category)} />
                      ))}
                    </Bar>
                  </BarChart>
                </ResponsiveContainer>
              </div>

              <div className="chart-card">
                <h3 className="chart-title">Category Distribution</h3>
                <ResponsiveContainer width="100%" height={400}>
                  <PieChart>
                    <Pie
                      data={preparePieData()}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}
                      outerRadius={120}
                      fill="#8884d8"
                      dataKey="value"
                    >
                      {preparePieData().map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip 
                      contentStyle={{ 
                        backgroundColor: 'rgba(255, 255, 255, 0.98)', 
                        border: '1px solid #e5e7eb',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
                      }}
                    />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </div>

            <div className="section-header">
              <h2 className="section-title">Section Analysis</h2>
              <p className="section-description">
                Detailed breakdown of paper sections by category
              </p>
            </div>

            <div className="headings-grid">
              {results.subHeadings.map((heading, index) => (
                <div key={index} className="heading-card" style={{ animationDelay: `${index * 50}ms` }}>
                  <div className="heading-header">
                    <div className="heading-rank">#{index + 1}</div>
                    <div className="heading-badge" style={{ backgroundColor: getCategoryColor(heading.category) }}>
                      {heading.category}
                    </div>
                  </div>
                  <h3 className="heading-name">{heading.name}</h3>
                  <p className="heading-description">{heading.description}</p>
                  <div className="heading-stats">
                    <div className="heading-stat">
                      <span className="heading-stat-label">Found in</span>
                      <span className="heading-stat-value">{heading.count} papers</span>
                    </div>
                    <div className="heading-percentage">
                      {heading.percentage.toFixed(1)}%
                    </div>
                  </div>
                  <div className="heading-bar">
                    <div 
                      className="heading-bar-fill" 
                      style={{ 
                        width: `${heading.percentage}%`,
                        backgroundColor: getCategoryColor(heading.category)
                      }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>

            <div className="section-header">
              <h2 className="section-title">Search Results</h2>
              <p className="section-description">
                Papers analyzed from search results
              </p>
            </div>

            <div className="results-list">
              {results.searchResults.map((result, index) => (
                <div key={index} className="result-card" style={{ animationDelay: `${index * 50}ms` }}>
                  <div className="result-position">{result.position}</div>
                  <div className="result-content">
                    <a href={result.link} target="_blank" rel="noopener noreferrer" className="result-title">
                      {result.title}
                    </a>
                    <div className="result-link">{result.displayLink}</div>
                    <p className="result-snippet">{result.snippet}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {!results && !loading && (
          <div className="empty-state">
            <div className="empty-icon">
              <BookOpen size={64} />
            </div>
            <h3 className="empty-title">Ready to Analyze</h3>
            <p className="empty-description">
              Enter a search query to analyze deep learning journal papers and discover common sub-headings and structural patterns.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
