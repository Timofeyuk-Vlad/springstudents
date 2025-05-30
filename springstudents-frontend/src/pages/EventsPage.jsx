import React from 'react';
import { Layout } from 'antd';
import EventTable from '../components/events/EventTable';

const { Content } = Layout;

const EventsPage = () => (
    <Content style={{ padding: '24px' }}>
        <h1 style={{ marginBottom: '20px' }}>Управление событиями</h1>
        <EventTable />
    </Content>
);

export default EventsPage;