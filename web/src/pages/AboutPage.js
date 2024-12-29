import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from "react-router-dom";
import { Container, Typography, TextField, Button, Box, Grid, Paper } from '@mui/material';
import IconButton from "@mui/material/IconButton"; // For the button with the QR code icon
import Modal from "@mui/material/Modal"; // For the modal
import QrCodeIcon from "@mui/icons-material/QrCode"; // For the QR code icon
import { QRCodeCanvas } from "qrcode.react"; // For generating QR codes


function AboutPage() {
  const routerLocation = useLocation();
  const [tableData, setTableData] = useState([]);
  const inputRefs = useRef({});
  const [oldTableData, setOldTableData] = useState([]);
  const [weddings, setWeddings] = useState([]); // Store weddings data
  const [chosenWedding, setChosenWedding] = useState([]);// Store selected wedding
  const [showQrModal, setShowQrModal] = useState(false); // State to control QR modal visibility

  


  useEffect(() => {
    const fetchTableData = async () => {
      try {
        const event = JSON.parse(localStorage.getItem("chosenWedding"))
        console.log("ABOUT: CHOSEN WEDDING",event)
        setChosenWedding(event)
        console.log("AFTER: EVENT", event.name)
        console.log("AFTER: CHOSEN WEDDING", chosenWedding.name)
        const response = await axios.get(`http://localhost:8080/api/tables/wedding-name/${event.name}`);
        console.log("GOT RESPONSE DATA", response)
        const dataWithTitles = response.data.map((table) => ({
          ...table,
          title: `Table ${table.name}`, // Add the title field
          guestCount: table.seats.length,
          guests: Array.from({ length: table.seats.length }, (_, index) => table.seats[index].guestName || '')

        }));
        setTableData(dataWithTitles); // Assuming the response contains the data array
      } catch (err) {
        console.error("Error fetching table data:", err);
      }
    };

    fetchTableData();
  }, []); 

  const handleWeddingSelection = (wedding) => {
    setChosenWedding(wedding);
  };

  const handleGuestCountChange = async (tableId, count) => {
    console.log("HANDLEING TABKE CHANGE")
    const updatedTables = await Promise.all(
      tableData.map(async (table) => {
        if (table.id === tableId) {
          const guestCount = parseInt(count, 10) || 0;
          const seatNum = guestCount
          // Perform async call to the server
          try {
            //add num guests to table
            console.log("TRYING TO MAKE THIS CALL")
            const response = await axios.put(`http://localhost:8080/api/tables/addGuest/${chosenWedding.name}/${tableId}/${guestCount}`);
            console.log('Database updated successfully:', response.data);
          } catch (error) {
            console.error('Error updating database:', error);
          }

          //add seat with null guest-name and connect to this table 
          try {
            //add num guests to table
            console.log("TRYING TO ADD SEAT TO DB", "table id:", tableId)
            const response = await axios.post(`http://localhost:8080/api/seats/${chosenWedding.name}/${tableId}/${seatNum}`);
            console.log('seat table created successfully:', response.data);
          } catch (error) {
            console.error('Error updating seat table:', error);
          }

  
          return {
            ...table,
            guestCount,
            guests: Array.from({ length: guestCount }, (_, index) => table.guests[index] || ' '),
            seats:  Array.from({ length: guestCount }, (_, index) => table.seats[index] || '')
          };
        }
        return table;
      })
    );
  
    setTableData(updatedTables);
  };

  const handleGuestNameChangeLocal = (tableId, guestIndex, name) => {
    // Update the local state immediately for a responsive UI
    const updatedTables = tableData.map((table) => {
      if (table.id === tableId) {
        const updatedGuests = [...table.guests];
        updatedGuests[guestIndex] = name; // Update the guest name locally
        return { ...table, guests: updatedGuests, seats: updatedGuests };
      }
      return table;
    });
    setTableData(updatedTables);
  };
  
  const handleGuestNameChangeBackend = async (tableId, guestIndex, name) => {
    // Update the backend after the input loses focus
    console.log("BACKEND HANDLE", name, tableId, guestIndex)
    const table = tableData.find((table) => table.id === tableId);
    try {
      //add num guests to table
      const seatNum = guestIndex + 1
      console.log("TRYING TO UPDATE SEAT NAME IN DB", "table id:", tableId, "guest name", name, "guest index+1:", seatNum )
      const response = await axios.put(`http://localhost:8080/api/seats/${chosenWedding.name}/${tableId}/${seatNum}/${name}`);
      console.log('seat table updated successfully:', response.data);
    } catch (error) {
      console.error('Error updating seat table:', error);
    }
  };
  
  const handleAddTable = async() => {
    const newTableId = tableData.length + 1;
    const response = await axios.post(`http://localhost:8080/api/tables/create/${chosenWedding.name}/${newTableId}`);
    setTableData([
      ...tableData,
      { id: newTableId, title: `Table ${newTableId}`, guestCount: 0, seats: [], guests: [] },
    ]);
  };

  const handleQrCodeClick = () => {
    setShowQrModal(true);
  };

  const handleModalClose = () => {
    setShowQrModal(false);
  };

  const qrLink = `http://localhost:3000/guest/${localStorage.getItem("username")}/${chosenWedding.name}`;


  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      {/* Display Chosen Wedding Name at the Top Center */}
      {chosenWedding && (
        <Typography variant="h4" align="center" gutterBottom>
          {chosenWedding.name}
        </Typography>
      )}

      {/* QR Code Button in Top-Right Corner */}
      <Box textAlign="right" sx={{ mt: 2 }}>
        <IconButton color="primary" onClick={handleQrCodeClick}>
          <QrCodeIcon />
        </IconButton>
      </Box>

      {/* Render Wedding Squares in the Upper Right Corner */}
     

    {/* <Container maxWidth="md" sx={{ mt: 4 }}> */}
      <Typography variant="h4" gutterBottom>
        Manage Tables and Guests
      </Typography>

      {tableData.map((table) => (
        <Paper key={table.id} sx={{ padding: 3, mb: 3 }}>
          <Typography variant="h5">{table.title}</Typography>

          <TextField
            type="number"
            label="How many guests at this table?"
            value={table.guestCount}
            onChange={(e) => handleGuestCountChange(table.id, e.target.value)}
            fullWidth
            sx={{ mt: 2, mb: 2 }}
          />

          <Grid container spacing={2}>
            {table.guests.map((guest, index) => (
              <Grid item xs={12} sm={6} key={index}>
                <TextField
                  label={`Name ${index + 1}`}
                  value={guest}
                  onChange={(e) => {
                    setOldTableData(tableData)
                    handleGuestNameChangeLocal(table.id, index, e.target.value)}}
                  onBlur={(e) =>
                    handleGuestNameChangeBackend(table.id, index, e.target.value)
                  }
                  fullWidth
                />
              </Grid>
            ))}
          </Grid>
        </Paper>
      ))}

      <Box textAlign="center" sx={{ mt: 3 }}>
        <Button variant="contained" onClick={handleAddTable}>
          Add Table
        </Button>
      </Box>
    {/* QR Code Modal */}
      <Modal open={showQrModal} onClose={handleModalClose}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
            textAlign: "center",
          }}
        >
          <Typography variant="h6" gutterBottom>
            Scan to Access
          </Typography>
          <QRCodeCanvas value={qrLink} size={200} />
          <Typography variant="body2" sx={{ mt: 2 }}>
            {qrLink}
          </Typography>
          <Button onClick={handleModalClose} sx={{ mt: 2 }}>
            Close
          </Button>
        </Box>
      </Modal>
    </Container>
  );
}

export default AboutPage;
