import React from 'react';
import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
import './Header.css';

const { Header } = Layout;

const AppHeader = () => {
    return (
        <Header className="app-header">
            <h1 className="logo">Student Management System</h1>
            <Menu
                theme="dark"
                mode="horizontal"
                defaultSelectedKeys={['students']}
                className="nav-menu"
                style={{
                    lineHeight: '64px',
                    borderBottom: 'none',
                    background: 'transparent'
                }}
            >
                <Menu.Item key="students">
                    <Link to="/students">Студенты</Link>
                </Menu.Item>
                <Menu.Item key="events">
                    <Link to="/events">События</Link>
                </Menu.Item>
                <Menu.Item key="barters">
                    <Link to="/barters">Обмены</Link>
                </Menu.Item>
            </Menu>
        </Header>
    );
};

export default AppHeader;