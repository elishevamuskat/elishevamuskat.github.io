import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Button,
  Card,
  CardContent,
  Typography,
  Grid,
  Container,
  Box,
  Modal,
  TextField,
} from "@mui/material";
import axios from "axios";

const WeddingSelectionPage = () => {
  const [weddings, setWeddings] = useState([]);
  const [modalOpen, setModalOpen] = useState(false); // Track modal visibility
  const [newWedding, setNewWedding] = useState({ name: "", date: "", location: "" }); // Form state
  const navigate = useNavigate();

  // Fetch weddings when the component loads
  useEffect(() => {
    const fetchWeddings = async () => {
      try {
        const weddingsResponse = await axios.get("http://localhost:8080/api/weddings/username");
        console.log("ALL WEDDINGS", weddingsResponse)
        setWeddings(weddingsResponse.data); // Assume the API returns an array of weddings
      } catch (error) {
        console.error("Error fetching weddings:", error);
      }
    };
    fetchWeddings();
  }, []);

  // Handle selecting a wedding
  const handleSelectWedding = (wedding) => {
    console.log("HOME: CHOSEN WEDDING", wedding)
    localStorage.setItem('chosenWedding', JSON.stringify(wedding));
    navigate(`/about`, { state: {chosenWedding: { wedding } }}); // Pass the wedding data to the new page
  };

  const handleDeleteWedding = async (weddingName) => {
    try {
      await axios.delete(`http://localhost:8080/api/weddings/name/${weddingName}`);
      setWeddings((prevWeddings) => prevWeddings.filter((wedding) => wedding.name !== weddingName));
    //   if (chosenWedding?.id === weddingId) {
    //     setChosenWedding(weddings[0] || null); // Set a new chosen wedding or null if the list is empty
    //   }
    } catch (err) {
      console.error('Error deleting wedding:', err);
    }
  };

  // Handle creating a new wedding
  const handleCreateWedding = async () => {
    try {
      const weddingsResponse = await axios.post(`http://localhost:8080/api/weddings/create/${newWedding.name}/${newWedding.location}`);
      console.log("new wedding", weddingsResponse)
      setWeddings([...weddings, weddingsResponse.data]); // Add the new wedding to the list
      setModalOpen(false); // Close the modal
      setNewWedding({ name: "", date: "", location: "" }); // Reset the form
    } catch (error) {
      console.error("Error creating wedding:", error);
    }
  };

  return (
    <Container>
      <Typography variant="h4" align="center" gutterBottom>
        Select a Wedding
      </Typography>
      <Grid container spacing={3}>
    {weddings.map((wedding) => (
        <Grid item xs={12} sm={6} md={4} key={wedding.id}>
        <Card
            sx={{
            cursor: "pointer",
            position: "relative", // Enable positioning for delete button
            "&:hover": { boxShadow: 6 },
            }}
        >
            <CardContent onClick={() => handleSelectWedding(wedding)}>
            <Typography variant="h5">{wedding.name}</Typography>
            <Typography variant="body2" color="text.secondary">
                {wedding.date}
            </Typography>
            </CardContent>
            {/* Delete Button */}
            <Button
            size="small"
            variant="contained"
            color="error"
            onClick={(e) => {
                e.stopPropagation(); // Prevent click conflict with selecting a wedding
                handleDeleteWedding(wedding.name);
            }}
            sx={{
                position: "absolute",
                top: 10,
                right: 10,
                minWidth: "auto",
                padding: 0,
                width: 24,
                height: 24,
                fontSize: 12,
                borderRadius: "50%",
            }}
            >
            ×
            </Button>
        </Card>
        </Grid>
    ))}
    <Grid item xs={12} sm={6} md={4}>
        <Box
        sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: "100%",
            border: "2px dashed #ccc",
            borderRadius: 2,
            cursor: "pointer",
            "&:hover": { backgroundColor: "#f5f5f5" },
        }}
        onClick={() => setModalOpen(true)} // Open the modal
        >
        <Typography variant="h6" color="primary">
            + Create New Event
        </Typography>
        </Box>
    </Grid>
    </Grid>

      {/* Modal for Creating a New Wedding */}
      <Modal open={modalOpen} onClose={() => setModalOpen(false)}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "background.paper",
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
          }}
        >
          <Typography variant="h6" gutterBottom>
            Create a New Wedding
          </Typography>
          <TextField
            label="Name"
            fullWidth
            margin="normal"
            value={newWedding.name}
            onChange={(e) =>
              setNewWedding((prev) => ({ ...prev, name: e.target.value }))
            }
          />
          <TextField
            label="Date"
            type="date"
            fullWidth
            margin="normal"
            InputLabelProps={{ shrink: true }}
            value={newWedding.date}
            onChange={(e) =>
              setNewWedding((prev) => ({ ...prev, date: e.target.value }))
            }
          />
          <TextField
            label="Location"
            fullWidth
            margin="normal"
            value={newWedding.location}
            onChange={(e) =>
              setNewWedding((prev) => ({ ...prev, location: e.target.value }))
            }
          />
          <Button
            variant="contained"
            fullWidth
            sx={{ mt: 2 }}
            onClick={handleCreateWedding}
          >
            Create Wedding
          </Button>
        </Box>
      </Modal>
    </Container>
  );
};

export default WeddingSelectionPage;




