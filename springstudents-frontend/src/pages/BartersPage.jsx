import React from 'react';
import { Layout } from 'antd';
import BarterTable from '../components/barters/BarterTable';

const { Content } = Layout;

const BartersPage = () => (
    <Content style={{ padding: '24px' }}>
        <h1 style={{ marginBottom: '20px' }}>Управление обменами</h1>
        <BarterTable />
    </Content>
);

export default BartersPage;