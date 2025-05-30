import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Tag, Modal, message } from 'antd';
import { DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { fetchBarters, deleteBarter, fetchStudents } from '../../services/api';
import BarterForm from './BarterForm';

const BarterTable = () => {
    const [barters, setBarters] = useState([]);
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [selectedBarter, setSelectedBarter] = useState(null);
    const [filteredInfo, setFilteredInfo] = useState({});
    const [sortedInfo, setSortedInfo] = useState({});

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        setLoading(true);
        try {
            const [bartersRes, studentsRes] = await Promise.all([
                fetchBarters(),
                fetchStudents(),
            ]);
            setBarters(bartersRes.data);
            setStudents(studentsRes.data);
        } catch (error) {
            message.error('Ошибка загрузки данных');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteBarter(id);
            message.success('Обмен удален');
            loadData();
        } catch (error) {
            message.error('Ошибка удаления');
        }
    };

    const handleChange = (pagination, filters, sorter) => {
        setFilteredInfo(filters);
        setSortedInfo(sorter);
    };

    const columns = [
        {
            title: 'Предмет',
            dataIndex: 'item',
            key: 'item',
        },
        {
            title: 'Статус',
            dataIndex: 'status',
            key: 'status',
            filters: [
                { text: 'Активен', value: 'active' },
                { text: 'Завершен', value: 'closed' },
            ],
            onFilter: (value, record) => record.status === value,
            render: (status) => (
                <Tag color={status === 'active' ? 'green' : 'red'}>
                    {status === 'active' ? 'Активен' : 'Завершен'}
                </Tag>
            ),
        },
        {
            title: 'Студент',
            dataIndex: 'studentId',
            key: 'student',
            render: (id) => {
                const student = students.find((s) => s.id === id);
                return student ? `${student.firstName} ${student.lastName}` : 'Неизвестно';
            },
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_, record) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => {
                            setSelectedBarter(record);
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
                        setSelectedBarter(null);
                        setIsModalVisible(true);
                    }}
                >
                    Добавить обмен
                </Button>
            </div>
            <Table
                dataSource={barters}
                columns={columns}
                loading={loading}
                rowKey="id"
                onChange={handleChange}
            />
            <BarterForm
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                barter={selectedBarter}
                onSuccess={loadData}
                students={students}
            />
        </>
    );
};

export default BarterTable;