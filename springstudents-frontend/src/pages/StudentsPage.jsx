import React from 'react';
import { Layout } from 'antd';
import StudentTable from '../components/students/StudentTable';

const { Content } = Layout;

const StudentsPage = () => (
    <Content style={{ padding: '24px' }}>
        <h1>Студенты</h1>
        <StudentTable />
    </Content>
);

export default StudentsPage;