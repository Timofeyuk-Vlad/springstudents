import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Layout } from 'antd';
import Header from './components/Header';
import StudentsPage from './pages/StudentsPage';
import EventsPage from './pages/EventsPage';
import BartersPage from './pages/BartersPage';
import './App.css';

const { Content } = Layout;

function App() {
    return (
        <Router>
            <Layout className="layout">
                <Header />
                <Content style={{ padding: '0 50px', marginTop: 64 }}>
                    <div className="site-layout-content">
                        <Routes>
                            <Route path="/students" element={<StudentsPage />} />
                            <Route path="/events" element={<EventsPage />} />
                            <Route path="/barters" element={<BartersPage />} />
                            <Route path="/" element={<StudentsPage />} />
                        </Routes>
                    </div>
                </Content>
            </Layout>
        </Router>
    );
}

export default App;