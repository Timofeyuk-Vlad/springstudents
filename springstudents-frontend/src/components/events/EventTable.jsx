import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Tag, DatePicker, Modal, message } from 'antd';
import { DeleteOutlined, EditOutlined, FilterOutlined } from '@ant-design/icons';
import { fetchEvents, deleteEvent, fetchStudents } from '../../services/api';
import EventForm from './EventForm';

const { RangePicker } = DatePicker;

const EventTable = () => {
    const [events, setEvents] = useState([]);
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [filteredInfo, setFilteredInfo] = useState({});
    const [sortedInfo, setSortedInfo] = useState({});

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        setLoading(true);
        try {
            const [eventsRes, studentsRes] = await Promise.all([
                fetchEvents(),
                fetchStudents(),
            ]);
            setEvents(eventsRes.data);
            setStudents(studentsRes.data);
        } catch (error) {
            message.error('Ошибка загрузки данных');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteEvent(id);
            message.success('Событие удалено');
            loadData();
        } catch (error) {
            message.error('Ошибка удаления');
        }
    };

    const handleDateFilter = (dates) => {
        if (dates && dates.length === 2) {
            setFilteredInfo({
                ...filteredInfo,
                date: [dates[0].toISOString(), dates[1].toISOString()],
            });
        } else {
            const { date, ...rest } = filteredInfo;
            setFilteredInfo(rest);
        }
    };

    const handleChange = (pagination, filters, sorter) => {
        setFilteredInfo(filters);
        setSortedInfo(sorter);
    };

    const columns = [
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Дата',
            dataIndex: 'date',
            key: 'date',
            render: (date) => new Date(date).toLocaleString(),
            filterDropdown: ({ setSelectedKeys, selectedKeys, confirm }) => (
                <div style={{ padding: 8 }}>
                    <RangePicker
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        value={selectedKeys}
                        onChange={(dates) => {
                            setSelectedKeys(dates || []);
                            confirm();
                            handleDateFilter(dates);
                        }}
                    />
                </div>
            ),
            filterIcon: (filtered) => (
                <FilterOutlined style={{ color: filtered ? '#1890ff' : undefined }} />
            ),
            onFilter: (value, record) => {
                const eventDate = new Date(record.date);
                return (
                    eventDate >= new Date(value[0]) &&
                    eventDate <= new Date(value[1])
                );
            },
        },
        {
            title: 'Участники',
            dataIndex: 'studentIds',
            key: 'students',
            render: (studentIds) => (
                <Space>
                    {studentIds?.map((id) => {
                        const student = students.find((s) => s.id === id);
                        return student ? (
                            <Tag key={id}>{student.firstName}</Tag>
                        ) : null;
                    })}
                </Space>
            ),
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_, record) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => {
                            setSelectedEvent(record);
                            setIsModalVisible(true);
                        }}
                    />
                    <Button
                        danger
                        icon={<DeleteOutlined />}
                        onClick={() => handleDelete(record.id)}
                    />
                </Space>
            ),
        },
    ];

    return (
        <>
            <div style={{ marginBottom: 16, textAlign: 'right' }}>
                <Button
                    type="primary"
                    onClick={() => {
                        setSelectedEvent(null);
                        setIsModalVisible(true);
                    }}
                >
                    Добавить событие
                </Button>
            </div>
            <Table
                dataSource={events}
                columns={columns}
                loading={loading}
                rowKey="id"
                onChange={handleChange}
            />
            <EventForm
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                event={selectedEvent}
                onSuccess={loadData}
                students={students}
            />
        </>
    );
};

export default EventTable;