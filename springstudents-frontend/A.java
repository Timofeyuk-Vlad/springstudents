import React, { useState, useEffect, useCallback } from 'react';
import { Table, Button, Space, Tag, Input, message } from 'antd';
import { DeleteOutlined, EditOutlined, SearchOutlined } from '@ant-design/icons';
import { fetchEvents, deleteEvent, fetchStudents as fetchAllStudentsForForm } from '../../services/api';
import EventForm from './EventForm';

const { Search } = Input;

const EventTable = () => {
    const [events, setEvents] = useState([]);
    const [allStudents, setAllStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [searchText, setSearchText] = useState('');

    const loadData = useCallback(async () => {
        setLoading(true);
        try {
            const [eventsRes, studentsRes] = await Promise.all([
                fetchEvents(),
                fetchAllStudentsForForm(),
            ]);
            setEvents(eventsRes.data || []);
            setAllStudents(studentsRes.data || []);
        } catch (error) {
            console.error('Ошибка загрузки данных:', error);
            message.error('Ошибка загрузки данных');
            setEvents([]);
            setAllStudents([]);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadData();
    }, [loadData]);

    const handleDelete = async (id) => {
        try {
            await deleteEvent(id);
            message.success('Событие удалено');
            loadData();
        } catch (error) {
            console.error('Ошибка удаления:', error);
            message.error('Ошибка удаления события');
        }
    };

    const handleModalOpen = (event = null) => {
        setSelectedEvent(event);
        setIsModalVisible(true);
    };

    const handleModalClose = () => {
        setIsModalVisible(false);
        setSelectedEvent(null);
    };

    const handleFormSuccess = () => {
        loadData();
        handleModalClose();
    };

    const columns = [
        {
            title: 'Название',
            dataIndex: 'name',
            key: 'name',
            sorter: (a, b) => a.name.localeCompare(b.name),
        },
        {
            title: 'Дата и время',
            dataIndex: 'date',
            key: 'date',
            render: (date) => date ? new Date(date).toLocaleString('ru-RU') : 'N/A',
            sorter: (a, b) => new Date(a.date) - new Date(b.date),
        },
        {
            title: 'Участники',
            dataIndex: 'studentIds',
            key: 'studentNames',
            render: (studentIds) => {
                if (!studentIds || studentIds.length === 0) {
                    return <Tag>Нет участников</Tag>;
                }
                return (
                    <Space wrap size={[0, 8]}>
                        {studentIds.map((id) => {
                            const student = allStudents.find((s) => s.id === id);
                            return student ? (
                                <Tag key={id} color="geekblue">{`${student.firstName} ${student.lastName}`.trim()}</Tag>
                            ) : (
                                <Tag key={id} color="default">ID: {id}</Tag>
                            );
                        })}
                    </Space>
                );
            },
        },
        {
            title: 'Описание',
            dataIndex: 'description',
            key: 'description',
            ellipsis: true,
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_, record) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => handleModalOpen(record)}
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

    const filteredEvents = events.filter(event =>
        event.name?.toLowerCase().includes(searchText.toLowerCase())
    );

    return (
        <>
            <Space style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                <Search
                    placeholder="Поиск по названию события"
                    allowClear
                    onSearch={value => setSearchText(value)}
                    onChange={e => setSearchText(e.target.value)}
                    style={{ width: 300 }}
                    enterButton
                />
                <Button
                    type="primary"
                    onClick={() => handleModalOpen()}
                >
                    Добавить событие
                </Button>
            </Space>
            <Table
                dataSource={filteredEvents}
                columns={columns}
                loading={loading}
                rowKey="id"
                scroll={{ x: 'max-content' }}
            />
            {isModalVisible && (
                <EventForm
                    visible={isModalVisible}
                    onCancel={handleModalClose}
                    event={selectedEvent}
                    onSuccess={handleFormSuccess}
                    students={allStudents}
                />
            )}
        </>
    );
};

export default EventTable;