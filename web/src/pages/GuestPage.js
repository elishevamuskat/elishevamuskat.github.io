import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import {
  Container,
  Typography,
  TextField,
  Box,
  Autocomplete,
} from "@mui/material";

const GuestPage = () => {
  const { username } = useParams(); // Extract the weddingName from the URL
  const { weddingName } = useParams(); // Extract the weddingName from the URL
  const [guests, setGuests] = useState([]); // Store guest list
  const [selectedGuest, setSelectedGuest] = useState(null); // Track the selected guest
  const [error, setError] = useState(""); // Track errors, if any

  // Fetch guest data when the component loads
  useEffect(() => {
    const fetchGuests = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/seats/all/${username}/${weddingName}`
        );
        console.log("SEATS RESPONSE", response.data)
        setGuests(response.data); // Assume the API returns an array of guests
      } catch (err) {
        setError("Failed to fetch guest list. Please try again later.");
        console.error("Error fetching guests:", err);
      }
    };

    fetchGuests();
  }, [weddingName]);

  const handleGuestSelection = (event, value) => {
    // Update the selected guest when a name is selected
    setSelectedGuest(value);
  };

  return (
    <Container maxWidth="sm" sx={{ textAlign: "center", mt: 8 }}>
      <Typography variant="h4" gutterBottom>
        Welcome to {weddingName}'s Wedding!
      </Typography>

      {/* Error Message */}
      {error && (
        <Typography variant="body1" color="error">
          {error}
        </Typography>
      )}

      {/* Guest Dropdown */}
      <Autocomplete
        options={guests.filter((guest) => guest.guestName)}
        getOptionLabel={(guest) => guest.guestName} // Assuming guest objects have a 'name' field
        onChange={handleGuestSelection}
        renderInput={(params) => (
          <TextField {...params} label="Find Your Name" variant="outlined" />
        )}
        sx={{ mt: 4 }}
      />

      {/* Personalized Welcome Message */}
      {selectedGuest && (
        <Box sx={{ mt: 6 }}>
          <Typography variant="h5" sx={{ fontFamily: "serif", fontStyle: "italic" }}>
            Welcome <strong>{selectedGuest.guestName}</strong>,
          </Typography>
          <Typography variant="h6" sx={{ fontFamily: "serif" }}>
            Your table is <strong>Table {selectedGuest.tableRef}</strong>.
          </Typography>
        </Box>
      )}
    </Container>
  );
};

export default GuestPage;
