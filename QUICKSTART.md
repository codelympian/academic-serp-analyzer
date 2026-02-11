# Quick Start Guide - ScholarScan

Get up and running with ScholarScan in under 5 minutes!

## Prerequisites Check

Before you begin, make sure you have:
- ✅ Java 17 or higher
- ✅ Maven 3.6 or higher
- ✅ Node.js 18 or higher
- ✅ npm (comes with Node.js)

### Check Your Versions

```bash
java -version
mvn -version
node -v
npm -v
```

## Option 1: Automated Setup (Recommended)

### For Mac/Linux:

```bash
cd academic-serp-analyzer
chmod +x setup.sh
./setup.sh
```

### For Windows:

```bash
cd academic-serp-analyzer
setup.bat
```

Then follow the instructions to start the backend and frontend.

## Option 2: Manual Setup

### Step 1: Start the Backend

Open a terminal and run:

```bash
cd academic-serp-analyzer/backend
mvn clean install
mvn spring-boot:run
```

Wait for the message: `Started AcademicSerpApplication in X seconds`

### Step 2: Start the Frontend

Open a **new terminal** and run:

```bash
cd academic-serp-analyzer/frontend
npm install
npm run dev
```

Look for the message: `Local: http://localhost:3000/`

### Step 3: Open the Application

Open your browser and navigate to:
```
http://localhost:3000
```

## Your First Analysis

1. **Enter a query**: Type something like "transformer deep learning" or "CNN image classification"

2. **Click Analyze Papers**: Wait a few seconds while the system processes your query

3. **Explore the results**:
   - View statistics at the top
   - Check out the sub-heading distribution charts
   - Browse detailed section analysis
   - Review the original SERP results

## Using Serper API (Optional)

By default, the system uses mock data. To use real Google search results:

1. **Get API Key**:
   - Go to [Serper.dev](https://serper.dev/)
   - Sign up for a free account (2,500 free searches!)
   - Copy your API key from the dashboard

2. **Configure the Backend**:
   - Open `backend/src/main/resources/application.properties`
   - Replace the placeholder value:
   ```properties
   serper.api.key=YOUR_ACTUAL_SERPER_API_KEY_HERE
   ```

3. **Restart the Backend**:
   ```bash
   # Stop the backend (Ctrl+C)
   mvn spring-boot:run
   ```

## Testing Without API Key

The system provides comprehensive mock data automatically, so you can:
- Test all features without Serper API credentials
- See realistic academic paper data
- Explore the full UI and visualizations
- Understand how the analysis works

## Example Queries to Try

- "transformer architecture deep learning"
- "CNN image classification"
- "GAN generative models"
- "reinforcement learning algorithms"
- "attention mechanism papers"
- "BERT language model"
- "neural architecture search"
- "meta-learning few-shot"

## Common Issues & Solutions

### Backend won't start

**Issue**: Port 8080 already in use
```
Solution: Kill the process using port 8080
Mac/Linux: lsof -ti:8080 | xargs kill -9
Windows: netstat -ano | findstr :8080
         taskkill /PID <PID> /F
```

### Frontend won't start

**Issue**: Port 3000 already in use
```
Solution: The frontend will automatically try port 3001
Or manually change port in vite.config.js
```

### Maven build fails

**Solution:**
1. Clear Maven cache: mvn clean
2. Run: mvn clean install -U
3. Check Java version is 17+
```

## Stop the Application

To stop the servers:

1. **Backend**: Press `Ctrl+C` in the backend terminal
2. **Frontend**: Press `Ctrl+C` in the frontend terminal

## Need Help?

- 📖 Read the full [README.md](README.md)
- 💡 Review the assignment requirements
- 🐛 Check error messages in browser console and terminal

---

**You're all set! Happy analyzing academic papers! 📚**
