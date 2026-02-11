# ScholarScan - Academic Paper Sub-Heading Analyzer

A comprehensive MVP for analyzing Search Engine Results Pages (SERP) to identify distinct sub-headings in journal papers focused on deep learning models. This system uses React for the frontend and Java Spring Boot for the backend, with multithreaded processing for efficient analysis.

![ScholarScan](https://img.shields.io/badge/Status-MVP-success)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![React](https://img.shields.io/badge/React-18.2.0-blue)

## 🎯 Features

- **Real-time SERP Analysis**: Fetches live search results from Serper API
- **Multithreaded Processing**: Efficient parallel processing of search results
- **Sub-Heading Extraction**: Identifies 18+ common sections in academic papers
- **Category Classification**: Groups sections into 5 meaningful categories
- **Interactive Visualizations**: 
  - Bar charts showing sub-heading frequency
  - Pie charts displaying category distribution
  - Detailed section breakdown cards
- **Academic UI**: Clean, scholarly design with professional typography
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices

## 🏗️ Architecture

```
academic-serp-analyzer/
├── backend/                    # Java Spring Boot Backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/academicserp/
│   │       │       ├── AcademicSerpApplication.java
│   │       │       ├── controller/
│   │       │       │   └── SearchController.java
│   │       │       ├── service/
│   │       │       │   └── SearchService.java
│   │       │       ├── model/
│   │       │       │   ├── SearchResult.java
│   │       │       │   ├── SubHeading.java
│   │       │       │   └── AnalysisResponse.java
│   │       │       └── dto/
│   │       │           └── SearchRequest.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
└── frontend/                   # React Frontend
    ├── src/
    │   ├── App.jsx
    │   ├── App.css
    │   └── main.jsx
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Node.js 18+** and npm
- **Serper API Key** (optional - get 2,500 free searches at https://serper.dev)

### Backend Setup

1. Navigate to the backend directory:
```bash
cd academic-serp-analyzer/backend
```

2. Configure API key (optional):
   - Open `src/main/resources/application.properties`
   - Add your Serper API key:
   ```properties
   serper.api.key=YOUR_SERPER_API_KEY
   ```
   - **Note**: If you don't have an API key, the system will use mock data for demonstration

3. Build and run the backend:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd academic-serp-analyzer/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## 📖 Usage

1. **Access the Application**: Open your browser and navigate to `http://localhost:3000`

2. **Enter a Search Query**: Type a deep learning related query (e.g., "transformer architecture", "CNN image classification", "GAN models")

3. **Analyze Results**: Click the "Analyze Papers" button to process the query

4. **View Insights**:
   - See total papers count and processing time
   - Explore sub-heading distribution charts
   - Review detailed section analysis with categories
   - Browse original SERP results

## 🔍 Sub-Headings Detected

The system identifies 18+ common sections in academic papers:

### Paper Structure
- Abstract
- Introduction
- Conclusion

### Research Content
- Related Work
- Methodology
- Experiments
- Results
- Contributions
- Baselines

### Technical Details
- Architecture
- Implementation
- Datasets
- Training Details

### Analysis
- Discussion
- Limitations
- Future Work
- Ablation Study

### Supporting
- References

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **HTTP Client**: Apache HttpClient 5
- **JSON Processing**: Jackson
- **HTML Parsing**: Jsoup
- **Multithreading**: Java ExecutorService

### Frontend
- **Framework**: React 18.2.0
- **Build Tool**: Vite
- **Charts**: Recharts
- **Icons**: Lucide React
- **HTTP Client**: Axios
- **Styling**: Custom CSS with scholarly design

### API Integration
- **Search API**: Serper API
- **Mock Data**: Fallback for testing without API keys

## 📊 API Endpoints

### Health Check
```
GET /api/search/health
```
Returns the API status

### Analyze Query
```
POST /api/search/analyze
Content-Type: application/json

{
  "query": "transformer deep learning",
  "maxResults": 10
}
```

**Response:**
```json
{
  "query": "transformer deep learning",
  "totalResults": 10,
  "searchResults": [...],
  "subHeadings": [...],
  "processingTimeMs": 234
}
```

## 🎨 Design Philosophy

The UI features a **clean, scholarly aesthetic** with:
- **Typography**: Merriweather (serif) for headings, Inter for body text
- **Color Scheme**: Light theme with professional blues and purples
- **Layout**: Paper-like design with subtle textures
- **Accessibility**: High contrast and clear hierarchy

## 🔄 Multithreading Implementation

The backend uses Java's `ExecutorService` with a thread pool of 10 threads to:
1. Analyze each SERP result in parallel
2. Extract sub-headings concurrently
3. Aggregate results efficiently
4. Minimize processing time

## 🧪 Testing with Mock Data

If you don't have Serper API credentials, the system automatically generates realistic mock data:
- 10 simulated academic papers
- Diverse sources (arXiv, IEEE, ACM, etc.)
- Realistic snippets with paper content
- Full sub-heading extraction and analysis

## 📝 Assignment Requirements Met

✅ Multithreaded program implementation  
✅ Returns distinct sub-headings from journal papers  
✅ Focuses on deep learning models  
✅ Includes visualization of results  
✅ React frontend  
✅ Java backend  
✅ Real API integration (Serper)  
✅ Professional UI/UX  

## 📚 Example Queries

Try these queries to see the system in action:
- "transformer architecture deep learning"
- "CNN image classification"
- "GAN generative models"
- "RNN natural language processing"
- "reinforcement learning deep Q networks"
- "attention mechanism neural networks"
- "BERT language model"
- "ResNet computer vision"

## 🚧 Future Enhancements

- [ ] PDF text extraction from paper links
- [ ] Citation network analysis
- [ ] Author identification and tracking
- [ ] Conference/journal classification
- [ ] Temporal trend analysis
- [ ] Multi-language support
- [ ] Export to BibTeX
- [ ] Collaboration features

## 📄 License

This project is created for educational purposes

## 🤝 Contributing

This is an academic project. For suggestions or improvements, please refer to the assignment guidelines.

---

**Built with 📚 for Academic Research Analysis**
