import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Tag, Input, message } from 'antd';
import { DeleteOutlined, EditOutlined, SearchOutlined } from '@ant-design/icons';
import { fetchStudents, deleteStudent } from '../../services/api';
import StudentForm from './StudentForm';
import '../../styles/Table.css';

const { Search } = Input;

const StudentTable = () => {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [searchText, setSearchText] = useState('');

    useEffect(() => {
        loadStudents();
    }, []);

    const loadStudents = async () => {
        setLoading(true);
        try {
            const { data } = await fetchStudents();
            setStudents(data);
        } catch (error) {
            message.error('Ошибка загрузки студентов');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteStudent(id);
            message.success('Студент удален');
            loadStudents();
        } catch (error) {
            message.error('Ошибка удаления');
        }
    };

    const columns = [
        {
            title: 'Имя',
            dataIndex: 'firstName',
            key: 'firstName',
        },
        {
            title: 'Фамилия',
            dataIndex: 'lastName',
            key: 'lastName',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'События',
            dataIndex: 'events',
            key: 'events',
            render: (events) => (
                <Space>
                    {events?.map((event) => (
                        <Tag key={event.id}>{event.name}</Tag>
                    ))}
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
                            setSelectedStudent(record);
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

    const filteredStudents = students.filter(student =>
        [student.firstName, student.lastName, student.email].some(field =>
            field?.toLowerCase().includes(searchText.toLowerCase())
        ));

    return (
        <div className="table-container">
            <div style={{ marginBottom: '1rem', display: 'flex', justifyContent: 'space-between' }}>
                <Search
                    placeholder="Поиск студентов"
                    allowClear
                    enterButton={<SearchOutlined />}
                    style={{ width: 300 }}
                    onChange={(e) => setSearchText(e.target.value)}
                />
                <Button
                    type="primary"
                    onClick={() => {
                        setSelectedStudent(null);
                        setIsModalVisible(true);
                    }}
                >
                    Добавить студента
                </Button>
            </div>

            <table className="table">
                <thead>
                <tr>
                    {columns.map((column) => (
                        <th key={column.key}>{column.title}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {filteredStudents.map((student) => (
                    <tr key={student.id}>
                        {columns.map((column) => {
                            if (column.render) {
                                return <td key={column.key}>{column.render(student[column.dataIndex], student)}</td>;
                            }
                            return <td key={column.key}>{student[column.dataIndex]}</td>;
                        })}
                    </tr>
                ))}
                </tbody>
            </table>

            <StudentForm
                visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                student={selectedStudent}
                onSuccess={loadStudents}
            />
        </div>
    );
};

export default StudentTable;